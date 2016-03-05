import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.stream.IntStream;

public class ShipmentDetails {

	public int trackingNumber;
	public String source;
	public String destination;
	
	public int totalNodesToTravel;
	public int currentCityNode = 0;
	public boolean isShipped = false;
	public boolean isDelivered = false;
	
	
	public int weight;
	public int length;
	public int breadth;
	public int height;
	public int totalNumberOfItems;
	
	public ArrayList<String> routeArrayList = new ArrayList<>();
	
	LinkedHashMap<String, Date> routeCityHashMap = new LinkedHashMap<>();
	
	public void PrintRoute()
	{
		for(String x : routeArrayList)
		{
			System.out.println(x);
		}
	}
	
	public String GetNextCity()
	{
		String city;
		
		
		city = routeArrayList.get(currentCityNode); 
		currentCityNode = currentCityNode + 1;
		
		return city;
	}
	
	public void FeedShipmentDetails(int trackingNumber,String source, String destination, ArrayList<String> routeArrayList)
	{
		this.trackingNumber = trackingNumber;
		this.source = source;
		this.destination = destination;
		this.routeArrayList = routeArrayList;
		this.totalNodesToTravel = routeArrayList.size();
		
		Random random = new Random();
		this.weight = random.nextInt((50 - 1) + 1) + 1; //((max-min)+1)+min
		this.length = random.nextInt((10 - 1) + 1) + 1;
		
		this.breadth = random.nextInt((10 - 1) + 1) + 1;
		this.height = random.nextInt((10 - 1) + 1) + 1;
		this.totalNumberOfItems = random.nextInt((3 - 1) + 1) + 1;
		
		Date date = new Date();
		routeCityHashMap.put(source, date);
		
		//currentCityNode = currentCityNode + 1;
	}
	
	public void UpdateRouteCity(Date date)
	{
		String city;
		//Date date = new Date();
		isShipped =true;
		
		if(!isDelivered)
		{
			if(currentCityNode < totalNodesToTravel)
			{
				city = GetNextCity();
				routeCityHashMap.put(city, date);
			}
			
			if(currentCityNode == totalNodesToTravel)
			{
				isDelivered = true;
			}
		}
		else if(isDelivered)
		{
			//package delivered
		}
		else
		{
			
		}
	}
	
	public void PrintTrackingDetails()
	{
		System.out.println();
		System.out.println("_____________________________________________________________________________________");
		System.out.println("\t" + "\t" +"\t" +"\t" + "Fedex Service" );
		System.out.println("_____________________________________________________________________________________");
		System.out.println("Tracking Number :"+ trackingNumber);
		
		System.out.println();
		if(isShipped)
		{
			System.out.println("Source : " + source + "\t" + "\t" + "Destination : " + destination);
		}
		else
		{
			System.out.println("Parcel with tracking id is not shipped");
		}
		System.out.println();
		
		for(Object x : routeCityHashMap.keySet())
		{
			System.out.println(x + "\t\t" + routeCityHashMap.get(x));
		}
		System.out.println();
		System.out.println();
		System.out.println();
		if(isShipped && !isDelivered)
		{
			System.out.println("Activity Status : In Transit...");
		}
		
		if(isDelivered)
		{
			System.out.println("Activity Status : Package Delivered...");
		}
		System.out.println("_____________________________________________________________________________________");
		System.out.println("Package Info :");
		System.out.println();
		System.out.println("Weight :" + weight + " lb");
		System.out.println("Dimensions :" + length + " X " + breadth + " X "  + height + " lb");
		System.out.println("Total Number of Pieces : " + totalNumberOfItems);
		System.out.println("_____________________________________________________________________________________");
		System.out.println();
	}
	
	public void GetSourceToDestinationRoute(int source, int destination)
	{
		
		
		ShortestPathAlgorithm shortestPath = new ShortestPathAlgorithm();
		shortestPath.GetAdjacancyMatrix(source,destination);
		shortestPath.Algorith();
		shortestPath.SourceToDestinationRoute();
	}
}
