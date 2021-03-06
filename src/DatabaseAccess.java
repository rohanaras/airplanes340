import java.util.Date;
import java.util.Vector;
import java.sql.* ;  // for standard JDBC programs
import java.util.*;
import javax.swing.JOptionPane;


public class DatabaseAccess {
	private static Connection conn;
	private static Map<String, Airport> airports;
	private static Map<Integer, Passenger> passengers;

	
	private static void createDatabaseAccess() {
		conn = getNewConnection();
	}

	private static Connection getNewConnection() {
		Connection connection = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//Set login info here
			String url = "jdbc:sqlserver://is-fleming.ischool.uw.edu";
			String user = "perry";
			String pass = "Info340C";

			connection = DriverManager.getConnection(url, user, pass);

			//Set database here
			connection.setCatalog("AirlineReservation");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static Airport[] GetAirportCities() {
		createDatabaseAccess();
		airports = new TreeMap<>();
		try{
			//Set the SQL query here
			String query = "SELECT airportCode, city FROM airport";

			//Call query and store in memory as rs
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			int i = 0;
			while(rs.next()){
				i++;
				airports.put(rs.getString("airportCode"), new Airport(i, rs.getString("city")));
			}

			Airport[] a = new Airport[airports.size()];
			for (Airport airport : airports.values()) {
				a[airport.AirportID - 1] = airport;
			}
			return a;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//Returns the most recent seat price for a given flightID
	public static float getCurrentPrice(int flight) {
		createDatabaseAccess();
		try{
			//Set the SQL query here
			//Selects the price for the most recent reservation given a flight number
			String query = "SELECT TOP 1 reservationPrice FROM Reservation" +
					" WHERE flightID = ? ORDER BY reservationDate DESC";

			//Prepare the statement
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, flight);

			//Call the query and store as rs
			ResultSet rs = preparedStatement.executeQuery();

			//While results has next, print name
			while(rs.next())
				return (rs.getFloat("reservationPrice"));

			//Keep base price if there are no passengers
			query = "SELECT basePrice FROM Flight WHERE flightID=?";
			PreparedStatement newStmt = conn.prepareStatement(query);
			newStmt.setInt(1, flight);
			rs = newStmt.executeQuery();
			while(rs.next())
				return (rs.getFloat("basePrice"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static Date getProperDateTime(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}

	public static Passenger [] GetCustomers () {
		createDatabaseAccess();
		passengers = new TreeMap<>();
		try{
			//Set the SQL query here
			//Returns passenger ID, fName, and lName
			String query = "SELECT passengerID, firstName, lastName FROM Passenger";

			//Call query and store in memory as rs
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			//While results has next, create new passenger
			while(rs.next()){
				passengers.put(rs.getInt("passengerID"), new Passenger(rs.getInt("passengerID"),
						rs.getString("firstName") + ' ' + rs.getString("lastName")));
			}

			Passenger[] p = new Passenger[passengers.size()];
			int i = 0;
			for (Passenger passenger : passengers.values()) {
				p[i] = passenger;
				i++;
			}
			return p;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Flight[] GetFlights(Airport DepartAirport, Airport ArriveAirport, Date DepartureDate ) {
		Vector<Flight> vFlights = new Vector<Flight>();
		try{
			//Set the SQL query here
			//Returns flights joined with aircraft for capacity

			String query = "SELECT * FROM Flight JOIN Aircraft ON Flight.aircraftID = Aircraft.aircraftID " +
					"WHERE originCode = (SELECT airportCode FROM Airport WHERE city = ?) AND destinationCode = " +
					"(SELECT airportCode FROM airport WHERE city = ?) AND scheduledDeptTime BETWEEN ? AND " +
					"DATEADD(day, 1, ?)";

			//Convert dateobjects
			Timestamp sqlDate = new Timestamp(DepartureDate.getTime());
			System.out.print("timestamp: " + sqlDate);

			//Prepare the statement
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, DepartAirport.toString());
			preparedStatement.setString(2, ArriveAirport.toString());
			preparedStatement.setTimestamp(3, sqlDate);
			preparedStatement.setTimestamp(4, sqlDate);

			//Call the query and store as rs
			ResultSet rs = preparedStatement.executeQuery();

			//While results has next, create flight and add it to vFlights
			while(rs.next()){
				Flight f = new Flight();
				f.FlightID = rs.getInt("flightID");
				f.FlightNumber = rs.getString("flightNumber");
				f.ArrivalAirport = airports.get(rs.getString("destinationCode"));
				f.DepartureAirport = airports.get(rs.getString("originCode"));
				f.ArrivalTime = rs.getTimestamp("scheduledArrTime");
				f.DepartureTime = rs.getTimestamp("scheduledDeptTime");
				f.BasePrice = rs.getFloat("basePrice");
				f.Capacity = rs.getInt("numberOfSeats");
				f.CurrentPrice = getCurrentPrice(f.FlightID);
				f.Reservations = null;	// Don't need to load these now.
				vFlights.add(f);
			}

			// Turn vFlights into the Flight array
			Flight [] arrFlights = new Flight[vFlights.size()];
			vFlights.toArray(arrFlights);

			//Return array of Flights
			return arrFlights;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Flight GetFlightDetails(int FlightID) {
		Flight f = new Flight();
		try{
			//Set the SQL query here
			//Selects the price for the most recent reservation given a flight number
			String query = "SELECT * FROM Flight WHERE flightID=?";

			//Call query and store in memory as rs
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, FlightID);
			ResultSet rs = stmt.executeQuery(query);

			//While results has next, print name
			while(rs.next()){
				f.FlightID = FlightID;
				f.ArrivalAirport = airports.get(rs.getString("destinationCode"));
				f.DepartureAirport = airports.get(rs.getString("originCode"));
				f.ArrivalTime = getProperDateTime(rs.getTimestamp("scheduledArrTime"));
				f.DepartureTime = getProperDateTime(rs.getTimestamp("scheduledDeptTime"));
				f.BasePrice = rs.getFloat("basePrice");
				f.CurrentPrice = 300; //TODO
				f.FlightNumber = rs.getString("flightNumber");
			}
			f.CurrentPrice = getCurrentPrice(FlightID);

			/** Getting capacity information **/

			query = "SELECT numberOfSeats FROM Flight JOIN Aircraft ON Flight.aircraftID=Aircraft.aircraftID";
			rs = stmt.executeQuery(query);

			while(rs.next())
				f.Capacity = rs.getInt("numberOfSeats");

			/** Getting the reservation information **/

			query = "SELECT mealType, seatNumber, reservationNotes, reservationPrice, firstName, lastName, " +
					"Passenger.passengerID FROM Reservation JOIN Passenger ON Reservation.PassengerID=Passenger.passengerID " +
					"WHERE flightID=" + FlightID;
			rs = stmt.executeQuery(query);

			if (passengers == null) GetCustomers();

			ArrayList<Reservation> reservations = new ArrayList<>();
			while(rs.next()) {
				Reservation r = new Reservation();
				r.Flight = f;
				r.MealOptions = rs.getString("mealType");
				r.Seat = rs.getString("seatNumber");
				int passID = rs.getInt("passengerID");
				if (!passengers.keySet().contains(passID)) GetCustomers();
				r.Passenger = passengers.get(passID);
				r.Passenger.Name = rs.getString("firstName") + " " + rs.getString("lastName");
				r.NotesAboutReservation = rs.getString("reservationNotes");
				r.PricePaid = rs.getFloat("reservationPrice");
				reservations.add(r);
			}
			f.Reservations = new Reservation[reservations.size()];
			f.Reservations = reservations.toArray(f.Reservations);

		}catch (Exception e) {
			e.printStackTrace();
		}

		return f;	
	}
	
	public static Reservation [] GetCustomerReservations(Passenger p) {
		createDatabaseAccess();
		try{
			//Set the SQL query here
			//Returns passenger ID, fName, and lName
			String query = "SELECT Flight.flightID, destinationCode, originCode, scheduledArrTime," +
					"scheduledDeptTime, basePrice, flightNumber, mealType, seatNumber, firstName," +
					"Reservation.passengerID, reservationNotes, reservationPrice FROM Reservation JOIN Flight " +
					"ON Flight.flightID = Reservation.flightID JOIN Passenger ON Reservation.passengerID = " +
					"Passenger.passengerID WHERE Reservation.passengerID = ?";

			//Prepare the statement
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, p.PassengerID);

			//Call the query and store as rs
			ResultSet rs = preparedStatement.executeQuery();

			//While results has next, create new passenger
			ArrayList<Reservation> al = new ArrayList<Reservation>();
			while(rs.next()){

				Reservation r = new Reservation();
				r.Flight = new Flight();
				int flightID = rs.getInt("flightID");
				r.Flight.ArrivalAirport = airports.get(rs.getString("destinationCode"));
				r.Flight.DepartureAirport = airports.get(rs.getString("originCode"));
				r.Flight.ArrivalTime = rs.getTimestamp("scheduledArrTime");
				r.Flight.DepartureTime = rs.getTimestamp("scheduledDeptTime");
				r.Flight.BasePrice = rs.getFloat("basePrice");
				r.Flight.CurrentPrice = getCurrentPrice(flightID);
				r.Flight.FlightNumber = rs.getString("flightNumber");
				r.MealOptions = rs.getString("mealType");
				r.Seat = rs.getString("seatNumber");
				r.Passenger = passengers.get(rs.getInt("passengerID"));
				r.Passenger.Name = rs.getString("firstName");
				r.Passenger.PassengerID = rs.getInt("passengerID");
				r.NotesAboutReservation = rs.getString("reservationNotes");
				r.PricePaid = rs.getFloat("reservationPrice");
				al.add(r);
			}

			Reservation [] arrReservation = new Reservation[al.size()];
			al.toArray(arrReservation);
			return arrReservation;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//DO NOT DO
	public static Reservation [] SearchReservationNotes(String query) {
		Reservation r = new Reservation();
		r.Flight = new Flight();
		r.Flight.ArrivalAirport = new Airport(0, "Seattle");
		r.Flight.DepartureAirport = new Airport(0, "Las Vegas");
		r.Flight.ArrivalTime = new Date();
		r.Flight.DepartureTime = new Date();
		r.Flight.BasePrice = 150;
		r.Flight.CurrentPrice = 300;
		r.Flight.FlightNumber = "154";
		r.MealOptions = "Steak";
		r.Seat = "14B";
		r.Passenger = passengers.get(0);
		r.Passenger.Name = "Kevin";
		r.NotesAboutReservation = "Has a baby.";
		r.PricePaid = 180;
		r.Relavance = 0.7;
		return new Reservation [] { r };
	}
	                    
	public static void MakeReservation(Flight f, Passenger p, String Seat, String Meal, String Notes) throws SQLException {
		Connection dbconn = getNewConnection();
		PreparedStatement stmt = null;

		try{
			/* begin transaction
			insert stuff
			if time is expired, rollback
			else commit
			 */

			dbconn.setAutoCommit(false);

			//Set the SQL query here
			String query = "INSERT INTO reservation VALUES (?,?,?,?,?,?,?);";
			stmt = dbconn.prepareStatement(query);
			stmt.setInt(1, p.PassengerID);
			stmt.setInt(2, f.FlightID);
			stmt.setString(3, Seat);
			stmt.setString(4, Meal);
			stmt.setFloat(5, f.CurrentPrice);
			Calendar cal = Calendar.getInstance();
			Timestamp datetime = new Timestamp(cal.getTimeInMillis());
			stmt.setTimestamp(6, datetime);
			stmt.setString(7, Notes);


			//Call query and execute
			stmt.executeUpdate();
			String q = "SELECT * FROM reservation WHERE reservation.flightID = ?";
			PreparedStatement s = dbconn.prepareStatement(q);
			s.setInt(1, f.FlightID);
			ResultSet rs = s.executeQuery();
			int count = 0;
			while(rs.next()){
				count++;
			}

			if (count <= f.Capacity) {
				dbconn.commit();

				JOptionPane.showMessageDialog(null, "Reservation on flight " + f.FlightNumber + " for " + p.Name +
						" in seat " + Seat + " eating " + Meal + " and with notes: " + Notes);
			} else {
				JOptionPane.showMessageDialog(null, "Error: max capacity reached");
			}


		}catch (Exception e) {
			e.printStackTrace();
			if (dbconn != null) {
				dbconn.rollback();
			}
		}finally {
			if (stmt != null) {
				stmt.close();
			}

			if (dbconn != null) {
				dbconn.close();
			}

		}
	}
}
