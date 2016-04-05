import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

public class HomeSchedulerThreadClass implements Runnable{

	Thread t = new Thread(this);
	
	HomeSchedulerThreadClass()
	{
		t.start();
	}
	
	
	public void run()
	{
		
		while(true){
			
			try {
				HomeEquipmentScheduler objHomeEquipmentScheduler = new HomeEquipmentScheduler();
				objHomeEquipmentScheduler.EquimentsScheduler();
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Date d = new Date();
			System.out.println("Hello World " + d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());
			try {
				t.sleep(300000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
