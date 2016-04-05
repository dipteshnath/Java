import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class UtilityScheduler {

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
	 {

	 private final static IntWritable one = new IntWritable(1);
	 private final static IntWritable zero = new IntWritable(1);
	 private Text word = new Text();
	 public static int count =0; 
	 public static Date date = new Date("2014/10/29".toString());
	 public List<Float> numbers = new ArrayList<>();
	 
	 public int bull = 0; 
	 public int bear = 0;
	 public int stagnant = 0;
	 
	 public int BullToBear = 0;
	 public int BullToStagnant = 0;
	 public int BullToBull = 0;
	 public int BearToBull = 0;
	 public int BearToBear = 0;
	 public int BearToStagnant = 0;
	 public int StagnantToBear = 0;
	 public int StagnantToBull = 0;
	 public int StagnantToStagnant = 0;
	 
	 public String PreviousState = "stagnant";
	 public String CurrentState = "";
	  
	 public static boolean MonitoringDay = false;
	 public int transition = 0;																																																																																																																																																																																																																																																																																																																																																																		 
	 
		 public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
		 {
		   StringTokenizer itr = new StringTokenizer(value.toString());
		   System.out.println(context);
		 }
	 }
	
	
	 public static class IntSumReducer
	 extends Reducer<Text,IntWritable,Text,DoubleWritable> {
	 private DoubleWritable result = new DoubleWritable();
	 public int count =0; 
	 public void reduce(Text key, Iterable<IntWritable> values,
	                    Context context
	                    ) throws IOException, InterruptedException {
	   int sum = 0;
	   double f = 0;
	   
	   for (IntWritable val : values) {
		   
	     sum += val.get();
	   }
	   f= (double)sum/51;
	   System.out.println(key +","+f);
	   result.set(f);
	   context.write(key, result);
	 }
	}
	 
	 
		public static ServerSocket welcomeSocket;
		public static Socket connectionSocket;
	 
	 public static void main(String[] args) throws IOException, ParseException, ClassNotFoundException, InterruptedException{
		 
		 
		 	//CostDetails objCD = new CostDetails();
		 	//objCD.GetCDailyCost();
		 String[] inputArray = null;
		 
		 /**
		  * Temp str to check commit
		  * */
		 //String str = "AC\nSat Nov 28 01:00:00 EST 2015\nSat Nov 28 23:59:59 EST 2015\n30\n\nCar Charger\nSat Nov 28 03:00:00 EST 2015\nSat Nov 28 04:00:00 EST 2015\n20\n\nDish Washer\nSat Nov 28 03:00:00 EST 2015\nSat Nov 28 03:30:00 EST 2015\n30\n\nDryer\nSat Nov 28 03:00:00 EST 2015\nSat Nov 28 04:00:00 EST 2015\n60\n\nFridge\nSat Nov 28 01:00:00 EST 2015\nSat Nov 28 23:59:59 EST 2015\n50\n\nModem\nSat Nov 28 01:00:00 EST 2015\nSat Nov 28 23:59:59 EST 2015\n10\n\nVaccum Cleaner\nSat Nov 28 03:00:00 EST 2015\nSat Nov 28 03:30:00 EST 2015\n30\n\nWashing Machine\nSat Nov 28 03:00:00 EST 2015\nSat Nov 28 03:30:00 EST 2015\n50\n\n";
		 
		 /*
	    	Utility ut = new Utility();
	    	ut.ApplianceScheduler("avadhootd",args);
	    	ut.CommitUserSchedule("avadhootd", args, str);
		 */
		 System.out.println("Utility Schedular UP....");
		 
		 welcomeSocket = new ServerSocket(8881);
			String str = "";
		 System.out.println("waiting for socket data....");	
			while(true){
			
				connectionSocket = welcomeSocket.accept();
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				
				try{
					System.out.println("In while..." + connectionSocket.isConnected());
				    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				    str = "";
				    //str = inFromClient.readLine();
				    
				    String line;
				    while(!(line = inFromClient.readLine()).contains("~"))
				    {
				    	System.out.println(line);
				    	str = str + line +"\n";
				    	if(line.contains("schedule"))
				    		break;
				    }
				    
				    System.out.println(str);
				    
				    //String[] inputArray;
				    inputArray = str.split("-");
				    
				    if(str.contains("schedule"))
				    {
				    	
				    	Utility scheduleUT = new Utility();
				    	scheduleUT.ApplianceScheduler(inputArray[1],args);
				    	
				    	outToClient.writeBytes(Utility.cost + "\n");
				    	
				    	for(String strg : Utility.AppliancesData.keySet()){
							Appliances obj = (Appliances) Utility.AppliancesData.get(strg);
							
							//System.out.println(obj.ApplianceName + "\n" + obj.StartTime + "\n" + obj.EndTime+ "\n" +obj.PowerRequirement+ "\n" + "\n");
							outToClient.writeBytes(obj.ApplianceName + "\n" + obj.StartTime + "\n" + obj.EndTime+ "\n" +obj.PowerRequirement+ "\n" );
						}
				    	
				    	System.out.println(Utility.cost);
				    }
				    else if(str.contains("commit"))
				    {
				    	System.out.println("In commit...");
				    	Utility commitUT = new Utility();
				    	commitUT.CommitUserSchedule(inputArray[1],args,inputArray[2]);// str should be replaced by args[2]
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
