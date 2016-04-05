import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;

public class EnergyMain {

	public static ServerSocket welcomeSocket;
	public static Socket connectionSocket;
	
	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		// TODO Auto-generated method stub
	    
		//#1
		//CostDetails ObjCostDetails = new CostDetails();
		//ObjCostDetails.GetCDailyCost();
		
		// #2
		//ArrayList<Integer> arr = new ArrayList(); 
				
		//CostCalculator objCostCalculator = new CostCalculator();
		//objCostCalculator.PowerToCostCalculator("avadhootd", arr);
		
		// #3
		
		
		//HomeSchedulerThreadClass t = new HomeSchedulerThreadClass();
		/*
		HomeEquipmentScheduler objHomeEquipmentScheduler = new HomeEquipmentScheduler();
		objHomeEquipmentScheduler.EquimentsScheduler();
		*/
		
		// #4
		String[] inputArray = null;
		Scheduler sh = new  Scheduler();
		sh.ApplianceScheduler();
		//sh.commitSchedlue();
		System.out.println("scheduling done...");
		welcomeSocket = new ServerSocket(8881);
		String str = "";
		
		while(true){
			
			str = "";
			
			connectionSocket = welcomeSocket.accept();
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			//Scanner scanner = new Scanner(System.in);				 
			//int trackingID = scanner.nextInt();
			try{
			System.out.println("In while..." + connectionSocket.isConnected());
		    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		    
		    //str = inFromClient.readLine();
		    
		    String line;
		    while(!(line = inFromClient.readLine()).contains("~"))
		    {
		    	System.out.println(line);
		    	str = str + line +"\n";
		    	if(line.contains("schedule") || line.contains("update"))
		    		break;
		    }
		    
		    System.out.println(str);
		    
		    //String[] inputArray;
		    inputArray = str.split("-");
			/*
			if(trackingID == 1){
				
				System.out.println("In Commit");
				sh.commitSchedlue();
				
			}
			else if(trackingID == 2){
				System.out.println("In Update");

				Date egUpdateStartDate = new Date();
				egUpdateStartDate.setHours(14);
				egUpdateStartDate.setMinutes(10);
				

				Date egUpdateEndDate = new Date();
				egUpdateEndDate.setHours(14);
				egUpdateEndDate.setMinutes(15);

				
				sh.updateApplianceSchedule("Lamp", egUpdateStartDate, egUpdateEndDate);
			}
			else if(trackingID == 3){
				System.out.println("In 5 Mins Power Update");
				
				Date egStartDate = new Date();
				egStartDate.setHours(14);
				egStartDate.setMinutes(10);
				

				Date egEndDate = new Date();
				egEndDate.setHours(14);
				egEndDate.setMinutes(15);
				
				sh.PowerConsumptionInFiveMinutesFrame(egStartDate, egEndDate);
				
			}
			else if(trackingID == 4){
				
				System.out.println("In Cost Table Update");
				
			}
			else if(trackingID == 5){
				
				System.out.println("In View Schedule");
				sh.printSchedule();
				
			}
			*/
		    
			    if(str.contains("update")){
			    	System.out.println("In Update");
	
			    	//String[] inputArray;
			    	
			    	inputArray = str.split("-");
			    	
					Date egUpdateStartDate = new Date(inputArray[2]);
					//egUpdateStartDate.setHours(14);
					//egUpdateStartDate.setMinutes(10);
					
	
					Date egUpdateEndDate = new Date(inputArray[3]);
					//egUpdateEndDate.setHours(14);
					//egUpdateEndDate.setMinutes(15);
	
					
					sh.updateApplianceSchedule(inputArray[1], egUpdateStartDate, egUpdateEndDate);
					//outToClient.writeBytes("true");
					System.out.println(Scheduler.cost + "\n");
					outToClient.writeBytes(Scheduler.cost + "\n");
					//System.out.println(Scheduler.cost + "\n");
					
					for(String strg : Scheduler.AppliancesData.keySet()){
						Appliances obj = (Appliances) Scheduler.AppliancesData.get(strg);
						
						outToClient.writeBytes(obj.ApplianceName + "\n" + obj.StartTime + "\n" + obj.EndTime+ "\n" +obj.PowerRequirement+ "\n");
					}
	
			    }
			    else if(str.contains("commit")){
					System.out.println("In Commit");
					
					sh.commitSchedlue("avadhootd",args,inputArray[2]);
					
					outToClient.writeBytes("commited");
			    }
			    else if(str.contains("schedule")){
			    	System.out.println("In View Schedule");
					//sh.printPreviewSchedule();
			    	outToClient.writeBytes(Scheduler.cost + "\n");
			    	
			    	for(String strg : Scheduler.AppliancesData.keySet()){
						Appliances obj = (Appliances) Scheduler.AppliancesData.get(strg);
						System.out.println(obj.ApplianceName + "\n" + obj.StartTime + "\n" + obj.EndTime+ "\n" +obj.PowerRequirement+ "\n");
						outToClient.writeBytes(obj.ApplianceName + "\n" + obj.StartTime + "\n" + obj.EndTime+ "\n" +obj.PowerRequirement+ "\n");
					}
			    	
			    }
			}
			catch(Exception e)
			{
				System.out.println("Exception In Main : " + e.getMessage());
				outToClient.writeBytes(str +"Tracking ID not Found");
			}
			finally
			{
				connectionSocket.close();
			
			}
		}
	}

}
