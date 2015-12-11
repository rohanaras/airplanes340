
public class Passenger {
	int PassengerID;
	String Name;

	public Passenger(int ID, String name) {
		PassengerID = ID;
		Name = name;
	}
	
	public String toString()
	{
		return Name;
	}
}
