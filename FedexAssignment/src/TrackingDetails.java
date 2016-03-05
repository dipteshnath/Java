import java.util.HashMap;
import java.util.Random;

public class TrackingDetails {

	String[][] TrackingMatrix = new String[10000][3];
	
	String source;
	String destination;
	int trackingNumber;
	
	public static HashMap<Integer, String> cityHashMap;
	HashMap<Integer, String> sourceCityHashMap;
	HashMap<Integer, String> destinationCityHashMap;
	
	TrackingDetails()
	{
		cityHashMap = new HashMap<>();
		cityHashMap.put(1, "Northborough, MA");
		cityHashMap.put(2, "Edison, NJ");
		cityHashMap.put(3, "Pittsburgh, PA");		
		cityHashMap.put(4, "Allentown, PA");
		cityHashMap.put(5, "Martinsburg, WV");
		cityHashMap.put(6, "Charlotte, NC");
		cityHashMap.put(7, "Atlanta, GA");
		cityHashMap.put(8, "Orlando, FL");
		cityHashMap.put(9, "Memphis, TN");
		cityHashMap.put(10, "Grove City, OH");
		cityHashMap.put(11, "Indianapolis, IN");
		cityHashMap.put(12, "Detroit, MI");
		cityHashMap.put(13, "New Berlin, WI");
		cityHashMap.put(14, "Minneapolis, MN");
		cityHashMap.put(15, "St. Louis, MO");
		cityHashMap.put(16, "Kansas, KS");
		cityHashMap.put(17, "Dallas, TX");
		cityHashMap.put(18, "Houston, TX");
		cityHashMap.put(19, "Denver, CO");
		cityHashMap.put(20, "Salt Lake City, UT");
		cityHashMap.put(21, "Phoenix, AZ");
		cityHashMap.put(22, "Los Angeles, CA");
		cityHashMap.put(23, "Chino, CA");
		cityHashMap.put(24, "Sacramento, CA");
		cityHashMap.put(25, "Seattle, WA");
		
	}
	
	public void Tracking()
	{
		//this.trackingNumber = trackingNumber;
		
		for(int i =1; i<=10; i++)
		{
			Random randomGenerator = new Random();
			
			int sourceCity= randomGenerator.nextInt(25);
			sourceCityHashMap.put(i, cityHashMap.get(sourceCity));
			
			int destinationCity = randomGenerator.nextInt(25);
			do
			{
				if(destinationCity != sourceCity && destinationCity == 0)
				{
					destinationCityHashMap.put(i, cityHashMap.get(destinationCity));;
				}
			}while(destinationCity == sourceCity || destinationCity == 0);
		}	
	}	
}
