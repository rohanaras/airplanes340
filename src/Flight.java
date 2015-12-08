import java.util.Date;


public class Flight {
	public int FlightID;
	public String FlightNumber;
	public Airport DepartureAirport;
	public Airport ArrivalAirport;
	public Date DepartureTime;
	public Date ArrivalTime;
	public float BasePrice;
	public float CurrentPrice;
	public int Capacity;
	
	public Reservation [] Reservations;
	
	public String toString()
	{
		return FlightNumber; 
	}
}
