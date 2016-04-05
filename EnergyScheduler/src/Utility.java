import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JTable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobConfigurable;
import org.apache.hadoop.mapred.JobContext;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.Utils.OutputFileUtils.OutputFilesFilter;
import org.apache.hadoop.mapred.lib.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

//import StockExchange.IntSumReducer;
//import StockExchange.TokenizerMapper;

public class Utility {


	public static JTable costPowerTable;
	String formattedDate = "";
	public static String[][] Data = new String[24][2];
	public static float cost =0;
	public static LinkedHashMap<String, String> equip = new LinkedHashMap();
	public static LinkedHashMap<String, Integer> DailyAutoSchedule = new LinkedHashMap<>();  
	public static LinkedHashMap<String, Appliances> AppliancesData = new LinkedHashMap<>();
	
	static int hourOfStart =0;
	
	public void  ApplianceScheduler(String userName,String[] args) throws ClientProtocolException, ParseException, IOException, ClassNotFoundException, InterruptedException{
		
		/* Create Table to Store Per Hour Power Costs with Descending order */
		/*
		TableColumn col0 = new TableColumn(0);
		col0.setHeaderValue("Time");

		TableColumn col1 = new TableColumn(1);
		col1.setHeaderValue("CostPerKWatt");

		TableColumn col2 = new TableColumn(2);
		col2.setHeaderValue("PowerThreshold");

		TableColumn col3 = new TableColumn(3);
		col3.setHeaderValue("Penalty");
		
		costPowerTable.addColumn(col0);
		costPowerTable.addColumn(col1);
		costPowerTable.addColumn(col2);
		costPowerTable.addColumn(col3);
		*/
		
		Date todaysDate = new Date();
		
		DateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = originalFormat.parse(todaysDate.toLocaleString());
		formattedDate = targetFormat.format(date); 
		System.out.println(formattedDate);
		
		//if(!DailyAutoSchedule.containsKey(formattedDate)){
			
			DailyAutoSchedule.put(formattedDate, 1);
			
			Date dt = new Date();
			//dt.setHours(0);
			String s = Integer.toString(dt.getHours()*100);
			hourOfStart = dt.getHours();
			//populatePerHourPowerValues(s);
			
			/*
			for (int i = 0; i < costPowerTable.getRowCount(); i++) {
				System.out.println("");
				for(int j = 0;j<4;j++){
					System.out.print(costPowerTable.getValueAt(i, j) + " ");
				}
			}
			*/
			
			populatePerHourPowerValuesFromFile(s);
			
			populateAppliances(userName);
			System.out.println("storing file...");
			storeAppliancesDetailsInFile(userName);
			
			/* 
			for(String str : AppliancesData.keySet()){
				Appliances obj = (Appliances) AppliancesData.get(str);
				
				System.out.println(obj.ApplianceID + " " + obj.ApplianceName +" "+ obj.Priority + " "+ obj.StartTime + " "+ obj.EndTime);
			}
			*/
			
		//}
		
		//ScheduleAppliance();
		
		
		/*To test user entered Schedule*/
		/*
		Appliances obj = AppliancesData.get("Microwave");
		Date egStartTime = new Date();
		egStartTime.setHours(2);
		egStartTime.setMinutes(0);
		
		Date egEndTime = new Date();
		egEndTime.setHours(4);
		egEndTime.setMinutes(0);
		
		obj.StartTime = egStartTime;
		obj.EndTime = egEndTime;
		*/
		
		
		/* Job for Scheduling */
		
		Configuration conf = new Configuration();
		conf.setBoolean("isSplitable", false);
		//conf.set
		conf.set("mapred.min.split.size", "536870912");

	    Job job = Job.getInstance(conf, "Utility");
	    
	    
	    
	    job.setJarByClass(Utility.class);
	    job.setNumReduceTasks(1);
	    job.setMapperClass(TokenizerMapper.class);
	    //job.setCombinerClass(IntSumReducer.class);
	    job.setReducerClass(IntSumReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    	    
	    job.waitForCompletion(true);

		
		//check();
		printSchedule();
		
		
		MoveScheduledFileToPreviewLocation(userName,args);
		
		getCostForSchedule();
	}
	
	public void printSchedule()
	{
		for(String str : AppliancesData.keySet()){
			Appliances obj = (Appliances) AppliancesData.get(str);
			
			System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);
		}
	}
	
	
public void populateAppliances(String userName) throws ClientProtocolException, IOException{
		
		AppliancesData = new LinkedHashMap<>();
		
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		InputStream inputStream = null;
		
		
		//nameValuePairs1.add(new BasicNameValuePair("UserName",userName));
		nameValuePairs1.add(new BasicNameValuePair("UserName","avadhootd"));
		
		HttpClient httpclient = new DefaultHttpClient();

		// have to change the ip here to correct ip
		HttpPost httppost = new HttpPost("http://10.38.103.37/getAppliancesForScheduling.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		inputStream = entity.getContent();
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
		
		String line = null;
		String[] LineArray;
		int i =11;
		
		while((line = reader.readLine()) != null){
			
			LineArray = line.split("-");
			
			AppliancesData.put(LineArray[1], new Appliances());
			
			Appliances obj = (Appliances) AppliancesData.get(LineArray[1]);
			
			obj.ApplianceID = LineArray[0];
			obj.ApplianceName = LineArray[1];
			obj.PowerRequirement = Integer.parseInt(LineArray[2]);
			obj.Priority = Integer.parseInt(LineArray[3]);
			obj.TimeToCompleteJob = Integer.parseInt(LineArray[4]);
			if(LineArray[5] == "Y" || LineArray[5] == "y")
				obj.DependanceFlag = Boolean.parseBoolean("true");
			obj.DependentAppliance = LineArray[6];
			
		}		
	}

	public void storeAppliancesDetailsInFile(String userName) throws FileNotFoundException, UnsupportedEncodingException{
		
		PrintWriter writer = new PrintWriter("/home/avadhoot/Desktop/UtilityServer/"+userName+".txt");
		
		
		
		for(String str : AppliancesData.keySet()){
			Appliances obj = (Appliances) AppliancesData.get(str);
			
			writer.println(obj.ApplianceName + "-" + obj.StartTime + "-" + obj.EndTime);
			//System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);
		}
		writer.close();
	}
	
public void populatePerHourPowerValuesFromFile(String hrs) throws ParseException, ClientProtocolException, IOException{
		
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		InputStream inputStream = null;
		
		equip =new LinkedHashMap<>();
		
		//BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
		BufferedReader br =  new BufferedReader(new FileReader("/home/avadhoot/Desktop/EnergyScheduling/CostPerHour.txt"));
		
		Data = new String[24][3];
		String[] columnNames = {"Time","CostPerKWatt","PowerThreshold"};
		String line = null;
		String[] LineArray;
		int i =0;
		while((line = br.readLine()) != null){
			//System.out.println(line);
			
			LineArray = line.split("-");
			if(Integer.parseInt(LineArray[0]) > (Integer.parseInt(hrs))){
			
				equip.put(LineArray[0], LineArray[1]);
				System.out.println(i);
				//compareValue = LineArray[0];
				//Element elem = new Element();
				//TableRow row = new TableRow(elem);
				Data[i][0] = LineArray[0];
				Data[i][1] = LineArray[1];
				Data[i][2] = LineArray[2];
				i++;
			}
		}

		costPowerTable = new JTable(Data, columnNames);
		
		for(String key : equip.keySet()){
			//System.out.println("In Equip Print...");
			System.out.println(key + " " + equip.get(key));
		}
		equip = sortByValues(equip);
		
		
		
		/*
		 equip.entrySet().stream()
	    	.sorted(Map.Entry.comparingByValue())
	    		.forEach(entry -> equip.values());
		*/
		System.out.println("");
		System.out.println("Sorted Order...");
		
		for(String key : equip.keySet()){
			System.out.println(key + " " + equip.get(key));
		}
		
	}


	/*
	 * Reference :http://beginnersbook.com/2013/12/how-to-sort-hashmap-in-java-by-keys-and-values/
	 * */
	private static LinkedHashMap sortByValues(HashMap map) { 
		LinkedList list = new LinkedList(map.entrySet());
    // Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
         public int compare(Object o1, Object o2) {
            return ((Comparable) ((Map.Entry) (o1)).getValue())
               .compareTo(((Map.Entry) (o2)).getValue());
         }
    });
		
		 LinkedHashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	}

	public static int PowerConsumptionInHour(Date date)
	{
		int power = 0;
		
		int hrs = date.getHours() * 100;
		
		int xPosition;
		
		for(int i =0;i<Data.length;i++)
		{
			if(Data[i].equals(hrs))
			{
				power = Integer.parseInt(Data[i][2]);
			}
		}
		
		
		return power;
		//costPowerTable.findComponentAt(x, 3);
		
	}
	
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
	 {

	 private final static IntWritable one = new IntWritable(1);
	 private final static IntWritable zero = new IntWritable(1);
	 private Text word = new Text();
	 public static int count =0; 
	 public static Date date = new Date("2014/10/29".toString());
	 //public List<Float> numbers = new ArrayList<>();
	 	 
	 public String PreviousState = "stagnant";
	 public String CurrentState = "";
	  
	 public static boolean MonitoringDay = false;
	 public int transition = 0;																																																																																																																																																																																																																																																																																																																																																																		 
	 
		 public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
		 {
			 
				int priorityIterator;
				int totalTimeForAppliance;
				int totalTimeSlice;
				int currentTimeSlice;
				int powerRequirement;
				Date StartTime = new Date();
				Date EndTime =  new Date();
				Date IteratorStartTime = new Date();
				Date IteratorEndTime = new Date();
				int flag = 0;
				int temp = 100;
				int coolingRate = 6;
			 
			 
			  StringTokenizer itr = new StringTokenizer(value.toString());
			  System.out.println(context);
			  
			  priorityIterator = Integer.parseInt(value.toString());
			  System.out.println("In Mapper...");
			 
			 for(String str : AppliancesData.keySet())
				{
				 
				 
				 	Appliances obj = (Appliances) AppliancesData.get(str);
					
					IteratorStartTime = new Date();
					IteratorEndTime = new Date();
					
					StartTime = new Date();
					EndTime =  new Date();
					if(obj.Priority == priorityIterator)
					{
						System.out.println("In Priority...");
						//obj.StartTime = new Date();
						//obj.StartTime.setHours(i);
						if(obj.StartTime == null || obj.EndTime == null || (!((obj.StartTime.getHours() <= StartTime.getHours()) && (obj.EndTime.getHours() >= StartTime.getHours()))))
						{
							System.out.println("In Date not Null...");
							
							totalTimeForAppliance = obj.TimeToCompleteJob;
							totalTimeSlice = totalTimeForAppliance/60;
							currentTimeSlice = 0;
							powerRequirement = obj.PowerRequirement;
							int count =0;
							
							if(totalTimeForAppliance == 1440)
							{
								StartTime.setHours(hourOfStart);
								StartTime.setMinutes(0);
								StartTime.setSeconds(0);
								
								EndTime.setHours(23);
								EndTime.setMinutes(59);
								EndTime.setSeconds(59);
								
								obj.StartTime = StartTime;
								
								obj.EndTime = EndTime;
								//printSchedule();
								System.out.println("");
								
								
								word = new Text();
								word.set(obj.ApplianceName+"-"+obj.StartTime+"-"+obj.EndTime+ "-" +obj.PowerRequirement  +",");
								
								context.write(word, one);
								System.out.println(word);
								
								
							}
							else
							{
								temp =100;
								for(String hour : equip.keySet())
								{
									IteratorStartTime.setHours(Integer.parseInt(hour)/100 -1);
									IteratorStartTime.setMinutes(0);
									IteratorStartTime.setSeconds(0);
									
									IteratorEndTime.setHours(Integer.parseInt(hour)/100);
									IteratorEndTime.setMinutes(0);
									IteratorEndTime.setSeconds(0);
									
									if((PowerConsumptionInHour(IteratorStartTime) + powerRequirement) < 1550)   //checking if valley or peak in that particular hour
									{
										if(AcceptanceProbability(IteratorStartTime,IteratorEndTime,temp)){
										StartTime.setHours(IteratorStartTime.getHours());
										StartTime.setMinutes(0);
										StartTime.setSeconds(0);
										
										EndTime.setHours(IteratorStartTime.getHours());
										EndTime.setMinutes(totalTimeForAppliance);
										EndTime.setSeconds(0);
								
										obj.StartTime = StartTime;
										
										obj.EndTime = EndTime;
										/*
										 * writing in context of output path 
										 * */
										
										word = new Text();
										word.set(obj.ApplianceName+"-"+obj.StartTime+"-"+obj.EndTime+ "-" +obj.PowerRequirement  +",");
										
										context.write(word, one);
										System.out.println(word);
										
										break;
										}
										else{
											temp = temp - coolingRate;
										}
								
									}
								}
							}
						}
					}
				}
			 
			 
			 
		 }
	 }
	
	public static boolean AcceptanceProbability(Date IteratorStartTime, Date IteratorEndTime, int temp){
		System.out.println(IteratorEndTime.getHours()*100);
		//System.out.println(equip.get(Integer.toString(IteratorEndTime.getHours()*100)));
		//System.out.println(Float.parseFloat(equip.get(compareValue)));
		
		//float str = Float.parseFloat(equip.get(Integer.toString(IteratorEndTime.getHours()*100)));
		if(IteratorEndTime.getHours()>0)
		{
			//if(Float.parseFloat(equip.get(Integer.toString(IteratorEndTime.getHours()*100))) > Float.parseFloat(equip.get(compareValue)))
		
			//{
			//	return true;
			//}
			
			//return false;
		
		Random R = new Random();
		Random J = new Random();
		
			if(R.nextInt(5)>R.nextInt(1)){
				return true;
			}
		}
		return false;
	}

	
	 public static class IntSumReducer
	 extends Reducer<Text,IntWritable,Text,IntWritable> {
	 private IntWritable result = new IntWritable();
	 public int count =0; 
	 public void reduce(Text key, Iterable<IntWritable> values,
	                    Context context
	                    ) throws IOException, InterruptedException {
	   int sum = 0;
	   double f = 0;
	   System.out.println("In Reducer...");
	   
	   for (IntWritable val : values) {
		   
	     sum += val.get();
	   }
	   //f= (double)sum/51;
	   System.out.println(key +","+sum);
	   result.set(sum);
	   context.write(key, result);
	  
	 }
	}

	 public void MoveScheduledFileToPreviewLocation(String userName, String[] args) throws IOException
	 {
		 BufferedReader br =  new BufferedReader(new FileReader(args[1]+"/part-r-00000"));

		 
		 PrintWriter writer = new PrintWriter("/home/avadhoot/Desktop/UtilityServer/PreviewSchedule/"+userName+".txt");
		 String line = "";
		 String[] array;
		 while((line = br.readLine()) != null)
		 {
			 array = line.split(",");
			 writer.println(array[0]);
			 
		 }
		 
		 writer.close();
	 }
	 
	 public void CommitUserSchedule(String userName, String[] args, String schedule) throws ClientProtocolException, IOException
		{
		 
		 	System.out.println("In Commit Schedule");
		 	getPreviewSchedule(userName,args,schedule);	//to get user schedule from preview schedule folder
		 	
			System.out.println("Appliances count : " + AppliancesData.size());
		 	
			
			for(String str : AppliancesData.keySet()){
				Appliances obj = (Appliances) AppliancesData.get(str);
				
				System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);

				ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
				InputStream inputStream = null;
				
				if(obj.StartTime.toString().equals("null") || obj.EndTime.toString().equals("null") || Integer.toString(obj.PowerRequirement).equals("null")){
					
				}
				else{
					nameValuePairs1.add(new BasicNameValuePair("EquipmentName",obj.ApplianceName));
					nameValuePairs1.add(new BasicNameValuePair("StartTime",obj.StartTime.toString()));
					nameValuePairs1.add(new BasicNameValuePair("EndTime",obj.EndTime.toString()));
					nameValuePairs1.add(new BasicNameValuePair("Power",Integer.toString(obj.PowerRequirement)));
					
					HttpClient httpclient = new DefaultHttpClient();
		
					// have to change the ip here to correct ip
					HttpPost httppost = new HttpPost("http://10.38.103.37/commitSchedule.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
					HttpResponse response = httpclient.execute(httppost);
		
					//System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);
				}
			}
			
			copyScheduleInCommitedFolder(userName, args);
			
			updatePowerFile(userName,args);
			
		}

	 
	 public void copyScheduleInCommitedFolder(String userName,String[] args) throws FileNotFoundException
	 {
		 PrintWriter writer = new PrintWriter(args[3]+userName+".txt");
		 
		 System.out.println("In commit schedule...");
		 
		 for(String str : AppliancesData.keySet()){
				Appliances obj = (Appliances) AppliancesData.get(str);
				
				writer.println(obj.ApplianceName + "-" + obj.StartTime + "-" + obj.EndTime + "-" + obj.PowerRequirement);
				System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);
			}
		 
		 writer.close();
	 }
	 
	 /*
	  * Updating power consumption file for Peak and Valley point
	  * */
	 public void updatePowerFile(String userName, String[] args) throws IOException
	 {
		 /*
		  * Loading HashMap for Power Calculation
		  * */
		 LinkedHashMap<Integer,Integer> powerPerHour = new LinkedHashMap<>();
		 for(int i =0;i<24;i++)
		 {
			 powerPerHour.put(i, 0);
		 }
		 
		 /*
		  * Calculating Power and Populating power HashMap
		  * */
		 for(String str : AppliancesData.keySet()){
				Appliances obj = (Appliances) AppliancesData.get(str);
				
				for(int i = 0;i<=24;i++)
				{
					if(obj.StartTime.getHours() == i)
					{
						powerPerHour.put(i, powerPerHour.get(i)+obj.PowerRequirement);
					}
					else if(obj.EndTime.getHours() == (i + 1))
					{
						powerPerHour.put(i, powerPerHour.get(i)+obj.PowerRequirement);
						
					}
					else if((i >= obj.StartTime.getHours()) && (i < obj.EndTime.getHours()))
					{
						powerPerHour.put(i, powerPerHour.get(i)+obj.PowerRequirement);
					}
					else if((i+1 > obj.StartTime.getHours()) && ((i+1) <= obj.EndTime.getHours()))
					{
						powerPerHour.put(i, powerPerHour.get(i)+obj.PowerRequirement);
					}
					else if((i <= obj.StartTime.getHours()) && ((i+1) >= obj.EndTime.getHours()))
					{
						powerPerHour.put(i, powerPerHour.get(i)+obj.PowerRequirement);
					}

				}
				
				//writer.println(obj.ApplianceName + "-" + obj.StartTime + "-" + obj.EndTime + "-" + obj.PowerRequirement);
				//System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);
			}
		 
		 /*
		  * Printing powerhashMap 
		  * */
		 for(Integer key : powerPerHour.keySet())
		 {
			 System.out.println(key +","+powerPerHour.get(key));
		 }
		 
		 /*
		  * Updating the Power File
		  * */
		 
		 /*Reading the current File*/
		 
		 BufferedReader br =  new BufferedReader(new FileReader("/home/avadhoot/Desktop/EnergyScheduling/CostPerHour.txt"));
			
			String[][] powerData = new String[24][3];
			String[] columnNames = {"Time","CostPerKWatt","PowerThreshold"};
			String line = null;
			String[] LineArray;
			int i =0;
			while((line = br.readLine()) != null){
				//System.out.println(line);
				
				LineArray = line.split("-");
				
					System.out.println(i);
					//compareValue = LineArray[0];
					//Element elem = new Element();
					//TableRow row = new TableRow(elem);
					powerData[i][0] = LineArray[0];
					powerData[i][1] = LineArray[1];
					powerData[i][2] = LineArray[2];
					i++;
				}
			
			br.close();
			/* Calculate the updated Power */
			System.out.println("i = "+ i);
			
			for(int j=0;j<i;j++)
			{
				System.out.println(j);
				//System.out.println(Integer.parseInt(powerData[j][0].toString().trim())/100);
				int power = Integer.parseInt(powerData[j][2]) +  powerPerHour.get(Integer.parseInt(powerData[j][0].toString().trim())/100 - 1);
				
				powerData[j][2] = Integer.toString(power);
			}
			
			System.out.println("power updated...");
		 /*
		  * Printing power Data in file
		  * */
			PrintWriter writer = new PrintWriter("/home/avadhoot/Desktop/EnergyScheduling/CostPerHour.txt");
			 
			 System.out.println("In updating Power values...");
			 
			 for(int j =0; j<i;j++){
					//Appliances obj = (Appliances) AppliancesData.get(str);
					
				 
					writer.println(powerData[j][0]+ "-" + powerData[j][1] + "-" + powerData[j][2]);
					System.out.println(powerData[j][0]+ "-" + powerData[j][1] + "-" + powerData[j][2]);
				}
			 
			 writer.close();
			
	 }
	 
	 public void getCostForSchedule()
	 {
		 cost =0;
		 for(String str : equip.keySet())
		 {
			 for(String strObj : AppliancesData.keySet()){
					Appliances obj = (Appliances) AppliancesData.get(strObj);
			
				if(obj.StartTime.getHours() == (Integer.parseInt(str)/100))
				{
					cost = cost + obj.PowerRequirement*Float.parseFloat(equip.get(str))/10;
					
				}
				else if(obj.EndTime.getHours() == (Integer.parseInt(str)/100 + 1))
				{
					cost = cost + obj.PowerRequirement*Float.parseFloat(equip.get(str))/10;
					
					
				}
				else if(((Integer.parseInt(str)/100) >= obj.StartTime.getHours()) && ((Integer.parseInt(str)/100) < obj.EndTime.getHours()))
				{
					cost = cost + obj.PowerRequirement*Float.parseFloat(equip.get(str))/10;
					
				}
				else if(((Integer.parseInt(str)/100)+1 > obj.StartTime.getHours()) && (((Integer.parseInt(str)/100)+1) <= obj.EndTime.getHours()))
				{
					cost = cost + obj.PowerRequirement*Float.parseFloat(equip.get(str))/10;
					
				}
				else if(((Integer.parseInt(str)/100) <= obj.StartTime.getHours()) && (((Integer.parseInt(str)/100)+1) >= obj.EndTime.getHours()))
				{
					cost = cost + obj.PowerRequirement*Float.parseFloat(equip.get(str))/10;
					
				}
			 }
		 }
	 }
	 
	public void getPreviewSchedule(String userName,String[] args, String schedule) throws IOException
	{
		
		
		AppliancesData = new LinkedHashMap<>();
		
		PrintWriter writer = new PrintWriter("/home/avadhoot/Desktop/UtilityServer/Temp/"+userName+".txt");
		
		writer.print(schedule);
		
		writer.close();
		
		BufferedReader br =  new BufferedReader(new FileReader("/home/avadhoot/Desktop/UtilityServer/Temp/"+userName+".txt"));
		
		System.out.println("In Get Preview Schedule...");
		
		Appliances obj;
		
		String line;
		String line1;
		String[] array;
		while(!(line = br.readLine()).equals(""))
		 {
			if(!(line1 = br.readLine()).equals('\n'))
			{
			System.out.println(line);
			 array = line.split("-");
			 
			 //writer.println(array[0]);
			 //System.out.println(array[0]);
			 AppliancesData.put(line.trim(), new Appliances());
				
			 obj = (Appliances) AppliancesData.get(line.trim());
				
			 
			 obj.ApplianceName = line;//array[0];
			 Date stDate = new Date(line1.trim());
			 Date endDate = new Date(br.readLine().trim());
			 obj.StartTime = stDate;
			 obj.EndTime = endDate;
			 obj.PowerRequirement = Integer.parseInt(br.readLine().trim());	
			 //br.readLine().trim();
			 
			 System.out.println(obj.ApplianceName + " " + obj.StartTime + " " + obj.EndTime + " " + obj.PowerRequirement);
			 //System.out.println("Appliances count : " + AppliancesData.size());
			} 
		 }
				
		System.out.println("Preview complete...");
		System.out.println(AppliancesData.size());
	}
}
