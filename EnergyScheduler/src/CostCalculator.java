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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class CostCalculator {

	String formattedDate = "";
	
	public void PowerToCostCalculator(String userName, LinkedHashMap<String, String> appliances) throws ClientProtocolException, IOException, ParseException{
		
		ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
		InputStream inputStream = null;
		
		int count = 1;
		
		Date todaysDate = new Date();
		
		DateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
		DateFormat targetFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = originalFormat.parse(todaysDate.toLocaleString());
		formattedDate = targetFormat.format(date); 
		System.out.println(formattedDate);

		
		StringBuilder sb = new StringBuilder();
		for(String key : appliances.keySet()){
			if(appliances.size() != count){
				sb.append("'"+key+"',");
				count++;
			}
			else
				sb.append("'"+key+"'");
		}
		
		nameValuePairs1.add(new BasicNameValuePair("UserName","avadhootd"));
		//nameValuePairs1.add(new BasicNameValuePair("EquipmentID","'01','02'"));
		nameValuePairs1.add(new BasicNameValuePair("EquipmentID",sb.toString()));
		
		//StringBuilder b = new StringBuilder();
		//b = PrintTrackingDetailsOnMail();
		
		//nameValuePairs1.add(new BasicNameValuePair("result",b.toString()));

		// Calculating Power
		
		
		
		HttpClient httpclient = new DefaultHttpClient();

		// have to change the ip here to correct ip
		HttpPost httppost = new HttpPost("http://192.168.0.110/getCostForSchedule.php");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		inputStream = entity.getContent();
		
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);

		String line = null;
		
		line = reader.readLine();
		System.out.println(line);
		//
		
		
		/* Calculating Cost based on Power and Hour and Penalty */
		ArrayList<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();
		InputStream inputStream1 = null;
		
		HttpClient httpclient1 = new DefaultHttpClient();

		// have to change the ip here to correct ip
		HttpPost httppost1 = new HttpPost("http://192.168.0.110/getCostOnPower.php");
		httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
		HttpResponse response1 = httpclient1.execute(httppost1);
		HttpEntity entity1 = response1.getEntity();
		inputStream1 = entity1.getContent();
		
		float costPerWatt = 0;
		float powerThreshold = 0;
		float penalty = 0;
		
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1, "iso-8859-1"), 8);

		String line1 = null;
		String[] powerCostArray;
		
		while((line1 = reader1.readLine()) != null){
			powerCostArray = line1.split("-");
			costPerWatt = Float.parseFloat(powerCostArray[0]);
			powerThreshold = Float.parseFloat(powerCostArray[1]);
			penalty = Float.parseFloat(powerCostArray[2]);
		}
		
		float totalCost;
		
		if(powerThreshold < Float.parseFloat(line))
			totalCost = (powerThreshold * costPerWatt) + ((Float.parseFloat(line)- powerThreshold)*penalty);
		else
			totalCost = powerThreshold * costPerWatt;
		
		
		System.out.println(totalCost);
	}
	
}
