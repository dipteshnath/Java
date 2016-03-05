import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ShipmentThreadClass implements Runnable{
	
	Thread t = new Thread(this);
	ArrayList<Integer> SourceToDestinationRouteArrayList = new ArrayList(); // to find out whether the system has already figured out the best route for this node
		// if the tracking number already exists in above arraylist then no need to find route for this because it will already be in system.
	
	int rangeStart;
	int rangeEnd = rangeStart + 1000;
	int rangeCounter = 0;
	
	public static HashMap<Integer, Object> trackingNumberObjects = new HashMap<>();
	
	ShipmentThreadClass()
	{
		
		t.start();
	}
	
	public void run()
	{
		int trackingNumber;
		Random randomGenerator = new Random();
		ShipmentDetails objTrack = new ShipmentDetails();
		
		for(int i=0;i<100000;i++)
		{
			trackingNumberObjects.put(i, new ShipmentDetails());
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		Date date = new Date();
		
		
		for(;;)
		{
			try
			{
				cal.add(Calendar.HOUR_OF_DAY, 1);
				date = cal.getTime();
				//rangeCounter = 99;
				rangeStart = rangeCounter * 1000;
				
				rangeStart = rangeStart + 1;
				
				for(int i = rangeStart ; i<rangeStart+999;i++)
				{
				
				
					//trackingNumber = randomGenerator.nextInt(100000);
					trackingNumber = i;
					
					
					if(!SourceToDestinationRouteArrayList.contains(trackingNumber))
					{
						SourceToDestinationRouteArrayList.add(trackingNumber);
						String source = FedexCourier.sourceCityHashMap.get(trackingNumber);
						String destination = FedexCourier.destinationCityHashMap.get(trackingNumber);
						ArrayList<Integer> routeArrayList = new ArrayList<>();
						
						int intSource = 0,intDestination = 0;
						for(int j = 0;j<25;j++)
						{
							if(FedexCourier.cityHashMap.get(j) == source)
							{	
								intSource = j;
								break;
							}
						}
						
						for(int j = 0;j<25;j++)
						{
							if(FedexCourier.cityHashMap.get(j) == destination)
							{	
								intDestination = j;
								break;
							}
						}
						//it should call one method to get source and destination to pass to adjacancy matrix 
						ShortestPathAlgorithm shortestPath = new ShortestPathAlgorithm();
						shortestPath.GetAdjacancyMatrix(intSource,intDestination);
						shortestPath.Algorith();
						routeArrayList = shortestPath.SourceToDestinationRoute();
						
						ArrayList<String> stringRouteArrayList = FedexCourier.GetStringRouteArrayList(routeArrayList);					
						
						//someArrayList.add(new ShipmentDetails());
						
						
						 
						objTrack = (ShipmentDetails)trackingNumberObjects.get(trackingNumber);
						
						//objTrack = (ShipmentDetails)obj;
						
						objTrack.FeedShipmentDetails(trackingNumber,source, destination, stringRouteArrayList);
						
						//objTrack.PrintRoute();
						
					}
					
					objTrack = (ShipmentDetails)trackingNumberObjects.get(trackingNumber);
					objTrack.UpdateRouteCity(date);
					//objTrack.PrintTrackingDetails();
				}
				rangeCounter++;
				
				if(rangeCounter == 100 || rangeStart >= 100000)
				{
					rangeStart = 0;
					rangeCounter = 0;
				}
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				System.out.println("Exception : "+e.getMessage());
			}
			
		}
	}
}
