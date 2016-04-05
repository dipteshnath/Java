import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Random;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.TableView.TableRow;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Scheduler {
	JTable costPowerTable;
	String formattedDate = "";
	public static float cost = 0;
	LinkedHashMap<String, String> equip = new LinkedHashMap();
	public static LinkedHashMap<String, Integer> DailyAutoSchedule = new LinkedHashMap<>();  
	public static LinkedHashMap<String, Appliances> AppliancesData = new LinkedHashMap<>();
	
	int hourOfStart =0;
	
	public void  ApplianceScheduler() throws ClientProtocolException, ParseException, IOException{
		
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
		
		if(!DailyAutoSchedule.containsKey(formattedDate)){
			
			DailyAutoSchedule.put(formattedDate, 1);
			
			Date dt = new Date();
			//dt.setHours(0);
			String s = Integer.toString(dt.getHours()*100);
			hourOfStart = dt.getHours();
			populatePerHourPowerValues(s);
			
			/*
			for (int i = 0; i < costPowerTable.getRowCount(); i++) {
				System.out.println("");
				for(int j = 0;j<4;j++){
					System.out.print(costPowerTable.getValueAt(i, j) + " ");
				}
			}
			*/
			
			populateAppliances();
			
			/* 
			for(String str : AppliancesData.keySet()){
				Appliances obj = (Appliances) AppliancesData.get(str);
				
				System.out.println(obj.ApplianceID + " " + obj.ApplianceName +" "+ obj.Priority + " "+ obj.StartTime + " "+ obj.EndTime);
			}
			*/
			
		}
		
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
		check();
		printSchedule();
		getCostForSchedule();
	}
	
	public void ScheduleAppliance(){
		
		//Appliances obj;
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
		
		for(priorityIterator = 5; priorityIterator>0; priorityIterator--){
			
			for(String str : AppliancesData.keySet()){
				flag = 0;
				Appliances obj = new Appliances();
				obj = (Appliances) AppliancesData.get(str);
				
				IteratorStartTime = new Date();
				IteratorEndTime = new Date();
				
				StartTime = new Date();
				EndTime =  new Date();
				
				Date currentDate = new Date();
				if((obj.StartTime == null || obj.EndTime == null))// || (!(obj.StartTime.getHours() <= currentDate.getHours()) && (obj.EndTime.getHours() >= currentDate.getHours())))
				{
					if(obj.Priority == priorityIterator){
						
						
						totalTimeForAppliance = obj.TimeToCompleteJob;
						totalTimeSlice = totalTimeForAppliance/60;
						currentTimeSlice = 0;
						powerRequirement = obj.PowerRequirement;
						int count =0;
						
						if(totalTimeForAppliance == 1440)
						{
							StartTime.setHours(0);
							StartTime.setMinutes(0);
							StartTime.setSeconds(0);
							
							EndTime.setHours(23);
							EndTime.setMinutes(59);
							EndTime.setSeconds(59);
							
							obj.StartTime = StartTime;
							
							obj.EndTime = EndTime;
							printSchedule();
							System.out.println("");
						}
						else
						{
							
							for(String hour : equip.keySet())
							{
								IteratorStartTime.setHours(Integer.parseInt(hour)/100 -1);
								IteratorStartTime.setMinutes(0);
								IteratorStartTime.setSeconds(0);
								
								IteratorEndTime.setHours(Integer.parseInt(hour)/100);
								IteratorEndTime.setMinutes(0);
								IteratorEndTime.setSeconds(0);
								
								if((PowerConsumptionInHour(IteratorStartTime, IteratorEndTime) + powerRequirement) < 150) //150 is threshold
								{
									StartTime.setHours(IteratorStartTime.getHours());
									StartTime.setMinutes(0);
									StartTime.setSeconds(0);
									
									if(totalTimeForAppliance <= 60)
									{
										EndTime.setHours(IteratorStartTime.getHours());
										EndTime.setMinutes(totalTimeForAppliance);
										EndTime.setSeconds(0);
										obj.StartTime = StartTime;
										
										obj.EndTime = EndTime;
										
										flag = 1;
										printSchedule();
										System.out.println("");
										break;
									}
									else
									{
										
										EndTime.setHours(IteratorEndTime.getHours());
										EndTime.setMinutes(0);
										EndTime.setSeconds(0);
										
										obj.StartTime = StartTime;
										
										obj.EndTime = EndTime;
										currentTimeSlice++;
										totalTimeForAppliance = totalTimeForAppliance - 60;
										do{
											count =0;
											flag = 0;
											if(totalTimeForAppliance <= 60)
											{
												EndTime.setHours(IteratorStartTime.getHours());
												EndTime.setMinutes(totalTimeForAppliance);
												EndTime.setSeconds(0);
												obj.EndTime = EndTime;
												currentTimeSlice++;
											}
											else
											{
												
												for(String iteratorHour : equip.keySet()){
													count++;
													IteratorStartTime.setHours(Integer.parseInt(iteratorHour)/100 -1);
													IteratorEndTime.setHours(Integer.parseInt(iteratorHour)/100);
														
													if((IteratorStartTime.getHours() != StartTime.getHours() || IteratorEndTime.getHours() != EndTime.getHours()) && currentTimeSlice != (totalTimeSlice+1))
													{
														if((StartTime.getHours() - IteratorStartTime.getHours()) == 1)
														{
															StartTime.setHours(IteratorStartTime.getHours());
															totalTimeForAppliance = totalTimeForAppliance - 60;
															currentTimeSlice++;
															flag = 1;
															obj.StartTime = StartTime;
															break;
														}
														
														if((IteratorEndTime.getHours() - EndTime.getHours()) == 1)
														{
															EndTime.setHours(IteratorEndTime.getHours());
															totalTimeForAppliance = totalTimeForAppliance - 60;
															currentTimeSlice++;
															flag = 1;
															obj.EndTime = EndTime;
															break;
														}
														if(flag == 1)
														{
															break;
														}
													}
													if(flag == 1)
													{
														break;
													}
												}
											}
											
										}while((currentTimeSlice != (totalTimeSlice+1)) && count < 24);
										
									}
									if(flag == 1)
									{
										break;
									}
								}
								if(flag == 1)
								{
									break;
								}
				
							}
							/*if(flag == 1)
							{
								break;
							}*/
			
						}
						/*if(flag == 1)
						{
							break;
						}*/
		
					}
					/*if(flag == 1)
					{
						break;
					}*/
	
				}
				
			}
			
		}
		
	}
	String compareValue = "";
	public void check(){
		int i=0;
		
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
		
		
		
		for(priorityIterator = 5;priorityIterator > 0;priorityIterator--)
		{
		
			for(String str : AppliancesData.keySet())
			{
				Appliances obj = (Appliances) AppliancesData.get(str);
				
				IteratorStartTime = new Date();
				IteratorEndTime = new Date();
				
				StartTime = new Date();
				EndTime =  new Date();
				if(obj.Priority == priorityIterator)
				{
					//obj.StartTime = new Date();
					//obj.StartTime.setHours(i);
					if(obj.StartTime == null || obj.EndTime == null || (!((obj.StartTime.getHours() <= StartTime.getHours()) && (obj.EndTime.getHours() >= StartTime.getHours()))))
					{
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
								
								if((PowerConsumptionInHour(IteratorStartTime, IteratorEndTime) + powerRequirement) < 155)
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
			//i++;
		}
	}
	
	public boolean AcceptanceProbability(Date IteratorStartTime, Date IteratorEndTime, int temp){
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
	
	public int PowerConsumptionInHour(Date starTime, Date endTime){
		int power =0;
		
		for(String str : AppliancesData.keySet()){
			Appliances obj = (Appliances) AppliancesData.get(str);
			
			Date startDate = obj.StartTime;
			Date endDate = obj.EndTime;
			
			//System.out.println(startDate.getHours());
			//System.out.println(endDate.getHours());
			if(obj.StartTime != null && obj.EndTime != null){
				
				if(starTime.getHours() == startDate.getHours())
				{
					power = power + obj.PowerRequirement;
				}
				else if(endTime.getHours() == endDate.getHours())
				{
					power = power + obj.PowerRequirement;
				}
				else if((startDate.getHours() >= starTime.getHours()) && (startDate.getHours() < endTime.getHours()))
				{
					power = power + obj.PowerRequirement;
				}
				else if((endDate.getHours() > starTime.getHours()) && (endDate.getHours() <= endTime.getHours()))
				{
					power = power + obj.PowerRequirement;
				}
				else if((startDate.getHours() <= starTime.getHours()) && (endDate.getHours() >= endTime.getHours()))
				{
					power = power + obj.PowerRequirement;
				}
				
				/*if((obj.StartTime.getHours() <= starTime.getHours()) && (obj.EndTime.getHours() >= endTime.getHours())){
					power = power + obj.PowerRequirement;
				}*/
			}
		}
		
		return power;
	}
	
	public int PowerConsumptionInFiveMinutesFrame(Date starTime, Date endTime){
		int power =0;
		
		for(String str : AppliancesData.keySet()){
			Appliances obj = (Appliances) AppliancesData.get(str);
			
			Date startDate = obj.StartTime;
			Date endDate = obj.EndTime;
			
			//System.out.println(startDate.getHours());
			//System.out.println(endDate.getHours());
			if(obj.StartTime != null && obj.EndTime != null){
				
				if(starTime.getHours() == startDate.getHours())
				{
					power = power + obj.PowerRequirement;
					System.out.println(obj.ApplianceName +" " +power);
				}
				else if(endTime.getHours() == endDate.getHours())
				{
					power = power + obj.PowerRequirement;
					System.out.println(obj.ApplianceName +" " +power);
				}
				else if((startDate.getHours() >= starTime.getHours()) && (startDate.getHours() < endTime.getHours()))
				{
					power = power + obj.PowerRequirement;
					System.out.println(obj.ApplianceName +" " +power);
				}
				else if((endDate.getHours() > starTime.getHours()) && (endDate.getHours() <= endTime.getHours()))
				{
					power = power + obj.PowerRequirement;
					System.out.println(obj.ApplianceName +" " +power);
				}
				else if((startDate.getHours() <= starTime.getHours()) && (endDate.getHours() >= endTime.getHours()))
				{
					power = power + obj.PowerRequirement;
					System.out.println(obj.ApplianceName +" " +power);
				}
				
				/*if((obj.StartTime.getHours() <= starTime.getHours()) && (obj.EndTime.getHours() >= endTime.getHours())){
					power = power + obj.PowerRequirement;
				}*/
			}
		}
		
		System.out.println(power);
		return power;
	}
	
	public void printSchedule()
	{
		System.out.println("In print schedule...");
		System.out.println(AppliancesData.size());
		for(String str : AppliancesData.keySet()){
			Appliances obj = (Appliances) AppliancesData.get(str);
			
			System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);
		}
	}
	
	public void printPreviewSchedule() throws IOException
	{
		EnergyMain.connectionSocket.setKeepAlive(true);
		DataOutputStream outToClient = new DataOutputStream(EnergyMain.connectionSocket.getOutputStream());
		
		for(String str : AppliancesData.keySet()){
			Appliances obj = (Appliances) AppliancesData.get(str);
			
			outToClient.writeBytes(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime+'\n');
		}
	}
	
	public void populateAppliances() throws ClientProtocolException, IOException{
		
		AppliancesData = new LinkedHashMap<>();
		
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		InputStream inputStream = null;
		
		
		nameValuePairs1.add(new BasicNameValuePair("UserName","avadhootd"));
		
		HttpClient httpclient = new DefaultHttpClient();

		// have to change the ip here to correct ip
		HttpPost httppost = new HttpPost("http://10.38.76.171/getAppliancesForScheduling.php");
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
	
	
	
	
	public void updateApplianceSchedule(String ApplianceName, Date StartTime, Date EndTime) throws ClientProtocolException, ParseException, IOException{
		
		Appliances obj = (Appliances) AppliancesData.get(ApplianceName);
		
		obj.StartTime = StartTime;
		obj.EndTime = EndTime;
		obj.Priority = 6;
		obj.UserOverrideFlag = true;
		
		equip = new LinkedHashMap();
		Date dt = new Date();
		//dt.setHours(0);
		String s = Integer.toString(dt.getHours()*100);
		hourOfStart = dt.getHours();
		populatePerHourPowerValues(s);
		
		ApplianceScheduler();
		System.out.println("Scheduling done...");
		//getCostForSchedule();
		//commitSchedlue();
	}
	
	public void populatePerHourPowerValues(String hrs) throws ParseException, ClientProtocolException, IOException{
		
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		InputStream inputStream = null;
		
		
		nameValuePairs1.add(new BasicNameValuePair("Date",formattedDate));
		nameValuePairs1.add(new BasicNameValuePair("Hours",hrs));
		
		
		//StringBuilder b = new StringBuilder();
		//b = PrintTrackingDetailsOnMail();
		
		//nameValuePairs1.add(new BasicNameValuePair("result",b.toString()));

		
		HttpClient httpclient = new DefaultHttpClient();

		// have to change the ip here to correct ip
		HttpPost httppost = new HttpPost("http://10.38.76.171/getCostInDesc.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		inputStream = entity.getContent();
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
		
		String[][] Data = new String[24][4];
		String[] columnNames = {"Time","CostPerKWatt","PowerThreshold","Penalty"};
		String line = null;
		String[] LineArray;
		int i =0;
		while((line = reader.readLine()) != null){
			//System.out.println(line);
			System.out.println(line);
			LineArray = line.split("-");
			equip.put(LineArray[0], LineArray[1]);
			compareValue = LineArray[0];
			//Element elem = new Element();
			//TableRow row = new TableRow(elem);
			Data[i][0] = LineArray[0];
			Data[i][1] = LineArray[1];
			Data[i][2] = LineArray[2];
			Data[i][3] = LineArray[3];
			i++;
		}

		costPowerTable = new JTable(Data, columnNames);
		
		for(String key : equip.keySet()){
			System.out.println(key + " " + equip.get(key));
		}
	}

	public void commitSchedlue(String userName,String[] args,String schedule) throws ClientProtocolException, IOException
	{
		
		System.out.println("In Commit Schedule");
	 	getPreviewSchedule(userName,args,schedule);	//to get user schedule from preview schedule folder
	 	
		System.out.println("Appliances count : " + AppliancesData.size());
	 	
		
		for(String str : AppliancesData.keySet()){
			Appliances obj = (Appliances) AppliancesData.get(str);
			

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
				HttpPost httppost = new HttpPost("http://10.38.76.171/commitSchedule.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
				HttpResponse response = httpclient.execute(httppost);
	
				System.out.println(obj.ApplianceName + "\t" + obj.StartTime + "\t" + obj.EndTime);
			}
		}
	}
	public void getPreviewSchedule(String userName,String[] args, String schedule) throws IOException
	{
		
		
		AppliancesData = new LinkedHashMap<>();
		
		PrintWriter writer = new PrintWriter("C:/Users/Diptesh/Desktop/EnergyScheduler/HomeServer/Temp/"+userName+".txt");
		
		writer.print(schedule);
		
		writer.close();
		
		BufferedReader br =  new BufferedReader(new FileReader("C:/Users/Diptesh/Desktop/EnergyScheduler/HomeServer/Temp/"+userName+".txt"));
		
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

}
