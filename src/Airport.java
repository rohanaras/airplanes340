
public class Airport {
	
	// TODO:  Change to your primary key if it is not an int..
	public int AirportID;
	public String AirportName;
	
	public Airport (int ID, String Name)
	{
		AirportID = ID;
		AirportName = Name;
	}
	public String toString() {
		return AirportName;
	} 
}
