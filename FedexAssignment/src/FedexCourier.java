import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class FedexCourier implements KeyListener {
	
	String[][] TrackingMatrix = new String[10000][3];
	
	String source;
	String destination;
	int trackingNumber;

	static int enterFlag = 0;
	
	public static HashMap<Integer, String> cityHashMap;
	public static HashMap<Integer, String> sourceCityHashMap;
	public static HashMap<Integer, String> destinationCityHashMap;
	
	//ShipmentThreadClass t1 = new ShipmentThreadClass();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FillCityCounter();
		Tracking();
		
		ShipmentThreadClass t1 = new ShipmentThreadClass();
		//t1.run();

		enterFlag = 0;
		HashMap<Integer, Object> trackingNumberObjects= ShipmentThreadClass.trackingNumberObjects;
		ShipmentDetails objTrack = new ShipmentDetails();
		
		for(;;)
		{
			/*if(enterFlag == 1)
			{*/
				try
				{
					
				//	t1.wait(300000);
				
					Scanner scanner = new Scanner(System.in);				 
					int trackingID = scanner.nextInt();
					//t1.wait(10000);

					objTrack = (ShipmentDetails)trackingNumberObjects.get(trackingID);
					
					objTrack.PrintTrackingDetails();
					
					
					
				}
				catch(Exception e)
				{
					
				}
				

			//}
		}
		
		/*ShortestPathAlgorithm shortestPath = new ShortestPathAlgorithm();
		shortestPath.GetAdjacancyMatrix();
		shortestPath.Algorith();
		shortestPath.SourceToDestinationRoute();
		*/
		
		
		/*Cities city = new Cities();
		city.cityCoordinates();*/
	}

	public static void FillCityCounter()
	{
		cityHashMap = new HashMap<>();
		cityHashMap.put(0, "Northborough, MA");
		cityHashMap.put(1, "Edison, NJ");
		cityHashMap.put(2, "Pittsburgh, PA");		
		cityHashMap.put(3, "Allentown, PA");
		cityHashMap.put(4, "Martinsburg, WV");
		cityHashMap.put(5, "Charlotte, NC");
		cityHashMap.put(6, "Atlanta, GA");
		cityHashMap.put(7, "Orlando, FL");
		cityHashMap.put(8, "Memphis, TN");
		cityHashMap.put(9, "Grove City, OH");
		cityHashMap.put(10, "Indianapolis, IN");
		cityHashMap.put(11, "Detroit, MI");
		cityHashMap.put(12, "New Berlin, WI");
		cityHashMap.put(13, "Minneapolis, MN");
		cityHashMap.put(14, "St. Louis, MO");
		cityHashMap.put(15, "Kansas, KS");
		cityHashMap.put(16, "Dallas, TX");
		cityHashMap.put(17, "Houston, TX");
		cityHashMap.put(18, "Denver, CO");
		cityHashMap.put(19, "Salt Lake City, UT");
		cityHashMap.put(20, "Phoenix, AZ");
		cityHashMap.put(21, "Los Angeles, CA");
		cityHashMap.put(22, "Chino, CA");
		cityHashMap.put(23, "Sacramento, CA");
		cityHashMap.put(24, "Seattle, WA");

	}
	
	public static void Tracking()
	{
		//this.trackingNumber = trackingNumber;
		sourceCityHashMap = new HashMap<>();
		destinationCityHashMap = new HashMap<>();
		for(int i =1; i<=100000; i++)
		{
			Random randomGenerator = new Random();
			
			int sourceCity= randomGenerator.nextInt(24);
			sourceCityHashMap.put(i, cityHashMap.get(sourceCity));
			
			int destinationCity ;
			do
			{
				destinationCity = randomGenerator.nextInt(24);
				if(destinationCity != sourceCity)
				{
					destinationCityHashMap.put(i, cityHashMap.get(destinationCity));;
				}
			}while(destinationCity == sourceCity);
		}	
	}	
	
	public static ArrayList<String> GetStringRouteArrayList(ArrayList<Integer> routeArrayList)
	{
		ArrayList<String> stringArrayList = new ArrayList<>();
		
		for(int x : routeArrayList)
		{
			stringArrayList.add(cityHashMap.get(x));
		}
		
		return stringArrayList;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println("Inside KeyEvent");
		 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
		      // Enter was pressed. Your code goes here.
			 //System.out.println("Inside IF");
			 enterFlag = 1;			 
		   }
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
