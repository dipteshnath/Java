import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;

public class ShortestPathAlgorithm {
	
	int numberOfNodes;
	double[][] adjacancyMatrix;
	Hashtable<Integer, Integer> hashTable;
	int source;
	Scanner scanner = new Scanner(System.in);
	HashSet<Integer> VisitedNodes = new HashSet();
	HashSet<Integer> NonVisitedNodes = new HashSet();
	double[] DistanceWeight; 
	int destination;

	
	public void GetAdjacancyMatrix()
	{
		
		//System.out.println("Enter Number of Nodes");
		//numberOfNodes = scanner.nextInt();
		numberOfNodes =25;
		adjacancyMatrix = new double[numberOfNodes][numberOfNodes];
		
		DistanceWeight = new double[numberOfNodes];
		
		System.out.println("Enter Source Node");
		source = scanner.nextInt();
		
		System.out.println("Enter Destination Node");
		destination = scanner.nextInt();
		
		Cities objCity = new Cities();
		adjacancyMatrix = objCity.cityCoordinates();
		
		
		//System.out.println("Enter Adjacancy Matrix Weights");
		
		// To keep minimum distance tracking of node with source in DistanceWeight Array
		for(int i=0;i<numberOfNodes;i++)
		{
			for(int j=0;j<numberOfNodes;j++)
			{
				//adjacancyMatrix[i][j] = scanner.nextInt();
				if(i==source) // if I matches with the source calculate the distance from source to evrey node as it is going to be the check-up value for minimum from source
				{
					DistanceWeight[j] = adjacancyMatrix[i][j];
				}
			}
		}
		
		/*
		for(int i=0;i<numberOfNodes;i++)
		{
			System.out.println("\n");
			for(int j=0;j<numberOfNodes;j++)
			{
				System.out.print(adjacancyMatrix[i][j] + "\t");
			}
		}*/
	}
	
	
	public void GetAdjacancyMatrix(int source, int destination)
	{
		
		//System.out.println("Enter Number of Nodes");
		//numberOfNodes = scanner.nextInt();
		this.source = source;
		this.destination = destination;
		numberOfNodes =25;
		adjacancyMatrix = new double[numberOfNodes][numberOfNodes];
		
		DistanceWeight = new double[numberOfNodes];
		
		/*System.out.println("Enter Source Node");
		source = scanner.nextInt();
		
		System.out.println("Enter Destination Node");
		destination = scanner.nextInt();
		*/
		Cities objCity = new Cities();
		adjacancyMatrix = objCity.cityCoordinates();
		
		
		//System.out.println("Enter Adjacancy Matrix Weights");
		
		// To keep minimum distance tracking of node with source in DistanceWeight Array
		for(int i=0;i<numberOfNodes;i++)
		{
			for(int j=0;j<numberOfNodes;j++)
			{
				//adjacancyMatrix[i][j] = scanner.nextInt();
				if(i==source) // if I matches with the source calculate the distance from source to evrey node as it is going to be the check-up value for minimum from source
				{
					DistanceWeight[j] = adjacancyMatrix[i][j];
				}
			}
		}
		
		/*
		for(int i=0;i<numberOfNodes;i++)
		{
			System.out.println("\n");
			for(int j=0;j<numberOfNodes;j++)
			{
				System.out.print(adjacancyMatrix[i][j] + "\t");
			}
		}*/
	}
	
	public void Algorith()
	{
		// hashTable is maintaining the node number as key and the another node number as value, but this value is next nearest node in the path of source to destination 
		hashTable = new Hashtable<>();
		
		for(int i = 0; i< numberOfNodes; i++)
		{
			hashTable.put(i, source);			// as currently the nearest node is going to be the source between every node so storing source node in value of each key node
			NonVisitedNodes.add(i);
		}
		
		/*
		for(int i = 0; i< hashTable.size(); i++)
		{
			System.out.println(hashTable.get(i));
		}
		*/

		VisitedNodes.add(source);
		hashTable.put(source, 0);
		NonVisitedNodes.remove(source);
		
		int nextNode;
		int currentNode = source;
		
		while(!(NonVisitedNodes.isEmpty()))
		{
			nextNode = getNextNode(currentNode);  // to get the next nearest node 
			//hashTable.put(nextNode, currentNode);
			
			//System.out.println("Next Node" + nextNode);
			updateDistancesFromNextNode(currentNode);   //updating the distances of every node with respect to the current node
			currentNode = nextNode;
			NonVisitedNodes.remove(nextNode);  
			VisitedNodes.add(nextNode);
			
		}
		/*
		for(int i = 0; i< hashTable.size(); i++)
		{
			System.out.println(i + "\t" + hashTable.get(i));
		} 
		*/
	}

	
	// To get next node with minimum distance from source
	public int getNextNode(int source) {
		// TODO Auto-generated method stub
		
		double min = 100000;
		int node = source;
		
		for(int i=0;i< numberOfNodes;i++)
		{
			if(!VisitedNodes.contains(i)) // excluding nodes which are visited
			{
				if(adjacancyMatrix[source][i] < min && i!=source)
				{
					min = adjacancyMatrix[source][i];
					node = i;
					//hashTable.put(i, source);
				}
			}
		}
		return node;
	}
	
	
	// to update the distances of every node from the current node
	public int updateDistancesFromNextNode(int currentNode) {
			
		double newDistance;
		for(int i=0;i< numberOfNodes;i++)
		{
			if(!VisitedNodes.contains(i))
			{
				newDistance = adjacancyMatrix[currentNode][i] + DistanceWeight[currentNode];
				
				if(newDistance <= DistanceWeight[i])
				{
					DistanceWeight[i] = newDistance;
					hashTable.put(i, currentNode);
				}
			}
		}
		return 0;
	}
	
	public ArrayList<Integer> SourceToDestinationRoute()
	{
		Stack<Integer> routeStack = new Stack<>();
		ArrayList<Integer> routeArrayList = new ArrayList<>();
		
		int currentNode = destination;
		
		routeStack.push(destination);
		
		while(currentNode != source)
		{
			routeStack.push(hashTable.get(currentNode));
			currentNode = hashTable.get(currentNode);
		}
		
		while(!routeStack.isEmpty())
		{
			int pop = routeStack.pop();
			routeArrayList.add(pop);
			//System.out.print(pop + "\t" + "->" + "\t");
		}
		
		return routeArrayList;
	}

}
