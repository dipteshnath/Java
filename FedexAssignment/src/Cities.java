
public class Cities {
	
	public double[][] cityCoordinates(){
		
		double[][] dist = new double[25][25];
		int i=0,j=0;
		for(i=0;i<25;i++)//set weight to highest value
		{ for(j=0;j<25;j++)
		{
		dist[i][j]=8000;
		}
		}

		for(i=0;i<25;i++)//if destination and source same put 0
		{ for(j=0;j<25;j++)
		if(i==j)
		{
		dist[i][j]=0;
		}
		}

		dist[0][3]=100;
		dist[1][3]=100;
		dist[2][3]=100;
		dist[3][0]=100;dist[3][1]=100;dist[3][2]=100;dist[3][4]=100;
		//dist[3][6]=100;
		dist[3][10]=100;
		dist[4][3]=100;
		dist[5][6]=100;
		dist[6][5]=100;dist[6][7]=100;dist[6][8]=100;dist[6][10]=100;
		//dist[6][15]=100;
		//dist[6][3]=100;
		dist[7][6]=100;
		dist[8][6]=100;
		dist[9][10]=100;
		dist[10][3]=100;dist[10][6]=100;dist[10][9]=100;dist[10][11]=100;dist[10][12]=100;dist[10][13]=100;dist[10][14]=100;dist[10][15]=100;
		dist[11][10]=100;
		dist[12][10]=100;
		dist[13][10]=100;
		dist[14][10]=100;
		//dist[15][6]=100;
		dist[15][10]=100;dist[15][16]=100;dist[15][17]=100;dist[15][18]=100;dist[15][19]=100;
		dist[16][15]=100;
		dist[17][15]=100;
		dist[18][15]=100;
		dist[19][15]=100;
		dist[20][21]=100;
		dist[21][20]=100;dist[21][22]=100;dist[21][23]=100;dist[21][24]=100;
		dist[21][15]=100;dist[15][21]=100;
		dist[22][21]=100;
		dist[23][21]=100;
		dist[24][21]=100;
		dist[0][3]=100;dist[1][3]=100;dist[2][3]=100;dist[4][3]=100;
		dist[5][6]=100;dist[7][6]=100;dist[8][6]=100;
		dist[9][10]=100;dist[11][10]=100;dist[13][10]=100;dist[14][10]=100;
		dist[16][15]=100;dist[17][15]=100;dist[18][15]=100;dist[19][15]=100;
		dist[20][21]=100;dist[22][21]=100;dist[23][21]=100;dist[24][21]=100;

		/*	
		for(i=0;i<25;i++)//if destination and source same put 0
		{ for(j=0;j<25;j++)
		{
		System.out.print(dist[i][j] + " ");
		}System.out.println();
		}
		*/
		/*String[][] distibution = new String[25][3];
   	 	String[][] distribution = {{"Northborough,MA","42.3194","71.6417"},{"Edison,NJ","40.5040","74.3494"},{"Pittsburgh,PA","40.4397","79.9764"},{"Allentown,PA","40.6017","75.4772"},{"Martinsburg,WV","39.4592","77.9678"},
   			 {"Charlotte,NC","35.2269","80.8433"},{"Atlanta,GA","33.7550","84.3900"},{"Orlando,FL","28.4158","81.2989"},{"Memphis,TN","35.1174","89.9711"}
   			 ,{"Grove City,OH","39.8781","83.0781"},{"Indianapolis,IN","39.7910","86.1480"},{"Detroit,MI","42.3314","83.0458"},{"New Berlin,WI","42.9792","88.1092"},{"Minneapolis,MN","44.9778","93.2650"},
   			 {"ST.Louis,MO","38.6272","90.1978"},{"Kansas,KS","39.1097","94.6764"},{"Dallas,TX","39.1067","94.6764"},{"Houston,TX","29.7604","95.3698"},{"Denver,CO","39.7392","104.9903"},{"Salt Lake City,UT","40.7500","111.8833"},
   			 {"Phoenix,AZ","33.4500","112.0667"},{"Los Angeles,LA","34.0500","118.2500"},{"Chino,CA","34.0178","117.6900"},{"Sacramento,CA","38.5556","121.4689"},{"Seattle,WA","47.6097","122.3331"}};
   	 	int i=0,j=0;
	   	double longitude1=0,latitude1=0,longitude2=0,latitude2=0;
	   	double [][] distance_arr=new double[25][25];//array to store 25x25 distance in mile matrix
	   	for(i=0;i<25;i++)//generate 25x25 matrix after calculating distance
	   	{	for(j=0;j<25;j++)
	   		{
	   			longitude1=Double.parseDouble(distribution[i][1]);
	   			latitude1=Double.parseDouble(distribution[i][2]);
	   			longitude2=Double.parseDouble(distribution[j][1]);
	   			latitude2=Double.parseDouble(distribution[j][2]);
	   			distance_arr[i][j]=distance(latitude1,longitude1,latitude2,longitude2);
	   		}
	   	}*/
   	/*
	   	for(i=0;i<25;i++)//printing the distance in 25x25 format
	   	{	for(j=0;j<25;j++)
	   		{
	   		System.out.print(distance_arr[i][j]+"  ");
	   		}System.out.println("");
	   	}
	   	*/
	   	
	   	return dist;
	}
	
	public double distance(double lat1,double lon1, double lat2, double lon2) {//Calculate the distance by using the logitude and latitude of the 2 location
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		 return (dist);
		}
		
	public double deg2rad(double deg) {//decimal degree to radians
		  return (deg * Math.PI / 180.0);
		}
		
	public double rad2deg(double rad) {//radians to decimal degree
		  return (rad * 180 / Math.PI);
		}
	
	public void CityHub()
	{
		
	}
}
