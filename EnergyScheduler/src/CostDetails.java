import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import org.apache.commons.httpclient.Header;
//import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class CostDetails {

	String formattedDate = "";
	
	public void GetCDailyCost() throws ClientProtocolException, IOException, ParseException{
		
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		InputStream inputStream = null;
		nameValuePairs1.add(new BasicNameValuePair("savefile","save file"));
		
		Date todaysDate = new Date();
		
		DateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = originalFormat.parse(todaysDate.toLocaleString());
		formattedDate = targetFormat.format(date); 
		System.out.println(formattedDate);
		
		HttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet("http://www.pjm.com//pub/account/lmpda/"+formattedDate+"-da.csv");
		Header header = new Header("Content-Type", "text/plain");
		httpget.addHeader("Content-Type", "text/plain");

		HttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
	
		//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/home/avadhoot/Desktop/EnergyScheduling/" + "data.csv", true)));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("C:/Users/Diptesh/Desktop/EnergyScheduler/" + "data.csv", true)));

		out.print(EntityUtils.toString(entity));
		out.close();
		getCostdetailsPerHour();
		
		
	}
	
	public void getCostdetailsPerHour() throws IOException{
		
		LinkedHashMap<String,String> costPerHourHashMap = new LinkedHashMap();
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();		
		BufferedReader br =  new BufferedReader(new FileReader("C:/Users/Diptesh/Desktop/EnergyScheduler/data.csv"));
		
		String line = "";
		int hour = 100;
		
		while ((line = br.readLine()) != null) {
			
			if(line.contains("Data")){
				line = br.readLine();
				String[] energyPrice = line.split(",");
				
				for(int i = 1;i<25;i++){
					costPerHourHashMap.put(Integer.toString(hour), Float.toString(Float.parseFloat(energyPrice[i])/1000));
					hour += 100 ;
				}
				break;
			}
			
		}
		System.out.println("printing costs in file....");
		
		PrintWriter out = new PrintWriter("C:/Users/Diptesh/Desktop/EnergyScheduler/CostPerHour.txt");
		
		for(String key : costPerHourHashMap.keySet()){
			System.out.println(key + " " + costPerHourHashMap.get(key));
			out.println(key + "-" + costPerHourHashMap.get(key)+"-"+ 0);
			
			nameValuePairs1.add(new BasicNameValuePair("Date",formattedDate));
			nameValuePairs1.add(new BasicNameValuePair("Time",key));
			nameValuePairs1.add(new BasicNameValuePair("CostPerKWatt",costPerHourHashMap.get(key)));
			nameValuePairs1.add(new BasicNameValuePair("PowerThreshold","100"));
			nameValuePairs1.add(new BasicNameValuePair("Penalty","0.1"));
			try{
			
				HttpClient httpclient = new DefaultHttpClient();
				
			HttpPost httppost = new HttpPost("http://10.38.76.171/costdetails.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
			HttpResponse response = httpclient.execute(httppost);
			}
			catch(Exception ex){
				
			}
			
			
		}
		out.close();
	}
	
}
