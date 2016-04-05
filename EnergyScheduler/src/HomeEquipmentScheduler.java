import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HomeEquipmentScheduler {

	String formattedDate = "";
	
	
	public void EquimentsScheduler() throws ClientProtocolException, IOException, ParseException{
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		InputStream inputStream = null;
		
		Date todaysDate = new Date();
		
		DateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = originalFormat.parse(todaysDate.toLocaleString());
		formattedDate = targetFormat.format(date); 
		System.out.println(formattedDate);
		
		nameValuePairs1.add(new BasicNameValuePair("UserName","avadhootd"));
		nameValuePairs1.add(new BasicNameValuePair("Date",formattedDate));

		LinkedHashMap<String, String> equip = new LinkedHashMap();
		
		//StringBuilder b = new StringBuilder();
		//b = PrintTrackingDetailsOnMail();
		
		//nameValuePairs1.add(new BasicNameValuePair("result",b.toString()));

		
		HttpClient httpclient = new DefaultHttpClient();

		// have to change the ip here to correct ip
		HttpPost httppost = new HttpPost("http://192.168.0.110/getSchedulingEquipments.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		inputStream = entity.getContent();
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
		
		String line = null;
		String[] LineArray;
		while((line = reader.readLine()) != null){
			//System.out.println(line);
			LineArray = line.split("-");
			equip.put(LineArray[0], LineArray[1]);
		}
		
		LinkedHashMap<String, String> proposedSchedule = new LinkedHashMap<>();
		
		int hours = todaysDate.getHours();
		Random rand = new Random();
		
		System.out.println(hours);
		
		for(String key : equip.keySet()){
			System.out.println(key+","+equip.get(key));
			if(equip.get(key).equals("AC") || equip.get(key).equals("Modem") || equip.get(key).equals("Fridge")){
				proposedSchedule.put(key, equip.get(key));
			}
			else if(equip.get(key).equals("Car Charger")){
				if(hours > 0 && hours < 6){
					proposedSchedule.put(key,  equip.get(key));
				}
			}
			else{
				if(rand.nextInt(10)>4){
					proposedSchedule.put(key, equip.get(key));
				}
			}
		}
		
		System.out.println("");
		for(String key : proposedSchedule.keySet()){
			System.out.println(key+","+proposedSchedule.get(key));
		}
	
		System.out.println("");
		
		CostCalculator objCostCalculator = new CostCalculator();
		objCostCalculator.PowerToCostCalculator("avadhootd", proposedSchedule);
		
		
	}
	
}
