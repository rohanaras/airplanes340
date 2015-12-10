import java.util.Date;
import java.util.Vector;
import java.sql.* ;  // for standard JDBC programs
import java.util.*;
import javax.swing.JOptionPane;


public class DatabaseAccess {
	private static Connection conn;

	public static void createDatabaseAccess() {
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//Set login info here
			String url = "jdbc:sqlserver://is-fleming.ischool.uw.edu";
			String user = "perry";
			String pass = "Info340C";

			conn = DriverManager.getConnection(url, user, pass);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Airport[] GetAirportCities()
	{
		createDatabaseAccess();
		try{
			//Set the SQL query here
			String query = "SELECT airport.city FROM airport";

			//Set database here
			conn.setCatalog("AirlineReservation");

			//Call query and store in memory as rs
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			//While results has next, print name
			ArrayList<String> al = new ArrayList<String>();
			while(rs.next()){
				al.add(rs.getString("city"));
			}

			Airport[] a = new Airport[al.size()];
			for (int i = 0; i < al.size(); i++) {
				a[i] = new Airport(i, al.get(i));
			}
			return a;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// TODO:  Query the database and retrive the information.
		// resultset.findcolumn(string col)
		//return new Airport[] { new Airport(1,"Seattle"), new Airport(2,"Portland") };
	}

	//Returns the most recent seat price for a given flightID
	public static float getCurrentPrice(int flight) {
		createDatabaseAccess();
		try{
			//Set the SQL query here
			String query = "SELECT TOP 1 reservationPrice FROM Reservation WHERE flightID = " + flight + " ORDER BY reservationDate DESC";

			//Set database here
			conn.setCatalog("AirlineReservation");

			//Call query and store in memory as rs
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			//While results has next, print name
			while(rs.next()){
				return(rs.getFloat("reservationPrice"));
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Flight[] GetFlights(Airport DepartAirport, Airport ArriveAirport, Date DepartureDate )
	{
		Vector<Flight> vFlights = new Vector<Flight>();
		try{
			//Set the SQL query here
			String query = "SELECT * FROM Flight JOIN Aircraft ON Flight.aircraftID = Aircraft.aircraftID";


			//Set database here
			conn.setCatalog("AirlineReservation");

			//Call query and store in memory as rs
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			//While results has next, print name
			while(rs.next()){
				Flight f = new Flight();
				f.FlightID = rs.getInt("flightID");
				f.ArrivalAirport = new Airport(1,"SEATTLE"); //need to work on airports
				f.DepartureAirport = new Airport(1,"Portland");
				f.ArrivalTime = rs.getTimestamp("scheduledArrTime");
				f.DepartureTime = rs.getTimestamp("scheduledDeptTime");
				f.BasePrice = rs.getFloat("basePrice");
				f.Capacity = rs.getInt("numberOfSeats");
				f.CurrentPrice = getCurrentPrice(f.FlightID);
				f.FlightNumber = rs.getString("flightNumber");
				f.Reservations = null;	// Don't need to load these now.
				vFlights.add(f);
			}

			Flight [] arrFlights = new Flight[vFlights.size()];
			vFlights.toArray(arrFlights);
			return arrFlights;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		// TODO:  Query the database and retrieve the information.
		// Loop through the row results, creating a new flight for each row.
		//  Add those flights to the vFlights vector.

		// DUMMY DATA FOLLOWS
//		Flight f = new Flight();
//		f.FlightID = 1;	// ID from the DB..
//		f.ArrivalAirport = new Airport(1,"Seattle");
//		f.DepartureAirport = new Airport(1,"Portland");
//		f.ArrivalTime = new Date();
//		f.DepartureTime = new Date();
//		f.BasePrice = 150;
//		f.Capacity = 10;
//		f.CurrentPrice = 300;
//		f.FlightNumber = "642";
//		f.Reservations = null;	// Don't need to load these now.
	
//		vFlights.add(f);
//
//		Flight [] arrFlights = new Flight[vFlights.size()];
//		vFlights.toArray(arrFlights);
//		return arrFlights;
	}

	public static Flight GetFlightDetails(int FlightID)
	{
		// TODO:  Query the database to get the flight information as well as all 
		// the reservations.
		
		// DUMMY DATA FOLLOWS
		Flight f = new Flight();
		f.FlightID = 1;	// ID from the DB..
		f.ArrivalAirport = new Airport(1,"Seattle");
		f.DepartureAirport = new Airport(1,"Portland");
		f.ArrivalTime = new Date();
		f.DepartureTime = new Date();
		f.BasePrice = 150;
		f.Capacity = 10;
		f.CurrentPrice = 300;
		f.FlightNumber = "642";
		
		Reservation r = new Reservation();
		r.Flight = f;
		r.MealOptions = "Steak";
		r.Seat = "14B";
		r.Passenger = new Passenger();
		r.Passenger.Name = "Kevin";
		r.NotesAboutReservation = "Has a baby.";
		r.PricePaid = 154;
		f.Reservations = new Reservation [] { r };
		
		return f;	
	}
	
	public static Passenger [] GetCustomers ()
	{
		createDatabaseAccess();
		try{
			//Set the SQL query here
			String query = "SELECT passengerID, firstName, lastName FROM Passenger";

			//Set database here
			conn.setCatalog("AirlineReservation");

			//Call query and store in memory as rs
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			//While results has next, print name
			ArrayList<Passenger> al = new ArrayList<Passenger>();
			while(rs.next()){
				Passenger p = new Passenger();
				p.Name = rs.getString("firstName");
				p.PassengerID = rs.getInt("passengerID");
				al.add(p);
			}

			Passenger [] arrPassenger = new Passenger[al.size()];
			al.toArray(arrPassenger);
			return arrPassenger;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;


		// TODO:  Query the database to retrieve a list of customers.
		
		// DUMMY VALUES FOLLOW
//		Passenger a = new Passenger();
//		a.Name = "Kevin";
//		Passenger b = new Passenger();
//		b.Name = "Niki";
//		Passenger c = new Passenger();
//		c.Name = "Ava";
//
//		return new Passenger [] {a,b,c};
	}
	
	public static Reservation [] GetCustomerRervations (Passenger p)
	{
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
		r.Passenger = new Passenger();
		r.Passenger.Name = "Kevin";
		r.NotesAboutReservation = "Has a baby.";
		r.PricePaid = 180;
		return new Reservation [] { r };
	}
	
	public static Reservation [] SearchReservationNotes(String query)
	{
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
		r.Passenger = new Passenger();
		r.Passenger.Name = "Kevin";
		r.NotesAboutReservation = "Has a baby.";
		r.PricePaid = 180;
		r.Relavance = 0.7;
		return new Reservation [] { r };
	}
	                    
	public static void MakeReservation(Flight f, Passenger p, String Seat, String Meal, String Notes)
	{
		// TODO: Insert data into your database.
		// Show an error message if you can not make the reservation.
		
		JOptionPane.showMessageDialog(null, "Reservation on flight " + f.FlightNumber + " for " + p.Name + " in seat " + Seat + " eating " + Meal + " and with notes: " + Notes);
	}
}
