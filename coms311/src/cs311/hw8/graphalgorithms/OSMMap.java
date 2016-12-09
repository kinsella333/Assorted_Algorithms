package cs311.hw8.graphalgorithms;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import javax.xml.parsers.*;

/**
 * Class to interface with XML representation of roads in the city of Ames as seen in the accompanied AmesMap.txt
 * @author Ray Kinsella
 *
 * @param <V> Vertex Data Generic
 * @param <E> Edge Data Generic
 */
public class OSMMap <V,E> {
	
	public IGraph<Location, Way> map;
	
	/**
	 * Basic constructor, initializes map and sets it to directed.
	 */
	public OSMMap(){
		map = new Graph<Location, Way>();
		map.setDirectedGraph();
	}
	
	/**
	 * Main Method for executing given arg text files.
	 * @param args MapFile.txt Routes.txt
	 */
	public static void main(String[] args) {
		OSMMap<Location,Way> t = new OSMMap<Location, Way>();
		List<Location> locList = new ArrayList<Location>();
		List<String> path = new ArrayList<String>(), pathSn = new ArrayList<String>();
		double time = System.currentTimeMillis();
		Location tempLoc;
		
		//Grab the MapFile
		String check = t.parse(args[0]);
		if(!check.equals(""))System.err.println(check);
		
		//Get route file
		File file = new File(args[1]);
		Scanner in;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println(e);
			return;
		}
		//Get each Location coords and add to list
		while(in.hasNextLine()){
			tempLoc = new Location(in.nextDouble(), in.nextDouble());
			
			locList.add(tempLoc);
			path.add(t.ClosestRoad(tempLoc));
		}
		
		List<String> TSP = t.ApproximateTSP(path);
		for(int i = 0; i < TSP.size(); i++){
			System.out.println(TSP.get(i));
		}
		
		/*
		//Loop through, collecting shortest route and print all the path's street routes 
		for(int i = 0; i < locList.size() - 1; i++){
			path = t.ShortestRoute(locList.get(i), locList.get(i+1));
			pathSn = t.StreetRoute(path);
			System.out.println("Begin Route: " + i + "\nStarting Node: " + path.get(0));
			for(int j = 0; j < pathSn.size(); j++){
				System.out.println("Road: " + pathSn.get(j));
			}
			System.out.println("Ending Node: " + path.get(path.size() -1) + "\n");
		}*/
		System.out.println((System.currentTimeMillis()- time)/1000);
		in.close();
	}
	
	/**
	 * Parser Function, which takes the filename as an argument. This function parses the 
	 * XML document and generates a graph from the Nodes contained there-in.
	 * Node that have element tag node and attribute, id, are added as vertices, and nodes with element tag 
	 * ways and attribute k with value highway and a name are added as edges.
	 * @param fileName
	 */
	public String parse(String fileName){
		//Document set up and conversion to UTF-8 format
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		File file = new File(fileName);
		Document doc = null;
		try {
			InputStream in = new FileInputStream(file);
			Reader reader = new InputStreamReader(in, "UTF-8");
			InputSource is = new InputSource(reader);
			
			builder = factory.newDocumentBuilder();
			doc = builder.parse(is);
		} catch (FileNotFoundException e1) {
			return e1.toString();
		} catch (ParserConfigurationException e) {
			return e.toString();
		} catch (SAXException e) {
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		}
		
		//As long as doc can be added to program, we normalize the contents.
		doc.getDocumentElement().normalize();
		
		//Create lists of all nodes and ways by searching for tags.
		NodeList locations = doc.getElementsByTagName("node");
		NodeList ways = doc.getElementsByTagName("way");
		
		//Precompute lengths to save time
		int wLength = ways.getLength();
		int locLength = locations.getLength();
		
		//Loop through all nodes in node lis
		for (int temp = 0; temp < locLength; temp++) {
			Node nNode = locations.item(temp);
			
			//If a node is a node element, get the lat and lon attributes. Then create the new vertex and add it to the map.
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               Location v = new Location(new Double(eElement.getAttribute("lat")), new Double(eElement.getAttribute("lon")));
	               String id = eElement.getAttribute("id");
	               map.addVertex(id, v);
			}
		}
		
		//Loop through the ways, checking to see if they are labled as oneways.
		for (int temp = 0; temp < wLength; temp++) {
			Node nNode = ways.item(temp);
			boolean nameB = false, hwB = false, oneway = false;
			
			//If we find an element node, begin to unwrap
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               NodeList ks = eElement.getElementsByTagName("tag");
	               String name = "";
	               
	               //For all k tags in element loop through and grab highway, name, and oneway.
	               for (int j = 0; j < ks.getLength(); j++) {
            		   Node aNode = ks.item(j);
            		   if(aNode.getNodeType() == Node.ELEMENT_NODE){
            			   Element aElement = (Element) aNode;
            			   if(aElement.getAttribute("k").equals("highway")){
            				   hwB = true;
            			   }
            			   if(aElement.getAttribute("k").equals("name")){
            				   name = aElement.getAttribute("v");
            				   nameB = true;
            			   }
            			   if(aElement.getAttribute("k").equals("oneway") && aElement.getAttribute("v").equals("yes")){
    	            		   oneway = true;
    	            	   }
            		   }
	               }
	               
	               //If the node has both highway and name tags
	               if(hwB && nameB){
	            	   NodeList nds = eElement.getElementsByTagName("nd");
	            	   String vertices[] = new String[nds.getLength()];
	            
	            	   //Loop through all listed references and store id's into an array
	            	   for (int j = 0; j < nds.getLength(); j++) {
	            		   Node aNode = nds.item(j);
	            		   if(aNode.getNodeType() == Node.ELEMENT_NODE){
	            			   Element aElement = (Element) aNode;
	            			   vertices[j] = aElement.getAttribute("ref"); 
	            		   }
	            	   }
	            	   
	            	   //Create edges between all listed ids and add them to the map.
	            	   for (int j = 0; j < vertices.length-1; j++) {
	            		   Location loc1 = (Location) map.getVertexData(vertices[j]);
	            		   Location loc2 = (Location) map.getVertexData(vertices[j+1]);
	            		   double dist = getDistance(loc1.getLatitude(), loc1.getLongitude(), loc2.getLatitude(), loc2.getLongitude());
	            		   
	            		   if(oneway){
	            			   map.addEdge(vertices[j], vertices[j+1], new Way(name, dist));    
	            		   }else{
	            			   
	            			   map.addEdge(vertices[j], vertices[j+1], new Way(name, dist));  
	            			   map.addEdge(vertices[j+1], vertices[j], new Way(name, dist));
	            		   }
	            	   }
	               }
			}
		}
		return "";
	}
	
	/**
	 * Approximation of the traveling sales person problem 
	 * @param vertices
	 * @return
	 */
	public List<String> ApproximateTSP(List<String> vertices){
		List<List<Edge<Way>>> paths = new ArrayList<List<Edge<Way>>>();
		List<String> vOrder = new ArrayList<String>();
		int size = vertices.size();
		String tempL;
		double edgeTotals[][] = new double[size][size];
		
		for(int i = 0; i < size; i++){
			tempL = vertices.get(i);
			for(int j = 0; j < size; j++){
				if(i != j){
					paths.add(GraphAlgorithms.ShortestPath(this.map, tempL, vertices.get(j)));
					edgeTotals[i][j] = this.map.getEdgeTotal();
				}
			}
		}
		
		List<Integer> order = TSPHelper(edgeTotals, size);
		double edgeTotal = 0;
		for(int i = 0; i < order.size(); i++){
			vOrder.add(vertices.get(order.get(i)));
		}
	
		for(int i = 0; i < order.size() - 2; i++){
			edgeTotal = edgeTotal + edgeTotals[i][i+1];
		}
		System.out.println(edgeTotal + "\n\n");
		return vOrder;
	}
	
	/**
	 * Helper Function for Approximate TPS, takes in an adjacency matrix, and calculates the mst for the given vertices.
	 * It then adds the initial vertex to the end of the list.
	 * 
	 * @param edgeTotals Total weight of each shortest path from every node to every other.
	 * @param size Number of vertices to traverse.
	 * @return Order of nodes to visit.
	 */
	private List<Integer> TSPHelper(double edgeTotals[][], int size){
		boolean visited[] = new boolean[size];
		List<Integer> out = new ArrayList<Integer>();
		Stack<Integer> stack = new Stack<Integer>();
		
		int index, dst = 0;
		double min;
		boolean flag = false;
		stack.push(0);
		
		//While stack has items in it keep looking to add vertex
		while(!stack.isEmpty()){
			index = stack.peek();
			min = Double.MAX_VALUE;
			
			//Iterate through and find min distance
			for(int i = 0; i < size; i++){
				if(edgeTotals[index][i] > 0 && !visited[i]){
					if(min > edgeTotals[index][i]){
						min = edgeTotals[index][i];
						dst = i;
						flag = true;
					}
				}
			}
			//If we found a min push on stack and add to output.
			if(flag){
				visited[dst] = true;
				stack.push(dst);
				out.add(dst);
				flag = false;
			}else{
				stack.pop();
			}
		}
		out.add(out.get(0));
		return out;
	}
	
	/**
	 * Computes the total mileage of road in the xml file.
	 * @return miles of road
	 */
	public double TotalDistance(){
		double dist = 0;
		List<Edge<Way>> t1 = map.getEdges();
		
		//Loop and add edge weight to total
		for(int i = 0; i < t1.size(); i++){
			dist += t1.get(i).getEdgeData().getWeight();
		}
		return dist/2;
		
	}
	
	/**
	 * Function to find the closest vertex id to the given location object.
	 * 
	 * @param loc Provided location object consisting of lat and lon values.
	 * @return id of closest vertex
	 */
	public String ClosestRoad(Location loc){
		String refId = "";
		List<Vertex<Location>> vList = map.getVertices();
		int size = vList.size();
		double min = Double.MAX_VALUE, temp = 0;
		double lat1 = loc.getLatitude();
		double long1 = loc.getLongitude();
		
		//Loop through all vertices, keeping track of the closest vertex which has edges coming from it.
		for(int i = 0; i < size; i++){
			temp = getDistance(lat1, long1, 
					vList.get(i).getVertexData().getLatitude(), vList.get(i).getVertexData().getLongitude());
			if(min > temp && map.getNeighbors(vList.get(i).getVertexName()).size() > 0){
				min = temp;
				refId = vList.get(i).getVertexName();
			}
		}
		return refId;
	}

	/**
	 * Function to find the Shortest Route between two given Location objects
	 * 
	 * @param loc1 Starting location
	 * @param loc2 Ending Location
	 * @return List of vertex ids representing the nodes traversed between start and end
	 */
	public List<String> ShortestRoute(Location loc1, Location loc2){
		String loc1ID = ClosestRoad(loc1);
		String loc2ID = ClosestRoad(loc2);

		//Call Dijkstra based algorithm to compute shortest path
		List<Edge<Way>> eList = GraphAlgorithms.ShortestPath(this.map, loc1ID, loc2ID);
		List<String> sList = new ArrayList<String>();
		int size = eList.size();
		
		//Add the vertices found along shortest path.
		for(int i = 0; i < size; i++){
			if(i == 0){
				sList.add(eList.get(i).getVertexName1());
				sList.add(eList.get(i).getVertexName2());
			}else{
				sList.add(eList.get(i).getVertexName2());
			}
		}
		return sList;
	}
	
	/**
	 * Function to take in the shortest route output and create a list of street names 
	 * representing the traversal between nodes.
	 * 
	 * @param ids List of ids from the Shortest Route function
	 * @return List of street names
	 */
	public List<String> StreetRoute(List<String> ids){
		List<String> sList = new ArrayList<String>();
		
		//Loop through adding edge names, and avoiding duplicates
		for(int i = 0; i < ids.size() - 1; i++){
			String e = map.getEdge(ids.get(i), ids.get(i+1)).getEdgeData().getName();
			if(!sList.contains(e)){
				sList.add(e);
			}
		}
		return sList;
	}
	/**
	 * Algorithm from //https://bigdatanerd.wordpress.com/2011/11/03/java-implementation-of-haversine-formula-for-distance-calculation-between-two-points/
	 * Computes the distance between two locations latitudes and longitudes.
	 * 
	 * @param lat1 Latitude of location one 
	 * @param long1 Longitude of location one 
	 * @param lat2 Latitude of location two 
	 * @param long2 Longitude of location two 
	 * @return the distance between the two locations.
	 */
	private double getDistance(double lat1, double long1, double lat2, double long2){
		double latDist = Math.toRadians(lat2-lat1);
		double longDist = Math.toRadians(long2-long1);
		
		double a = Math.sin(latDist / 2) * Math.sin(latDist / 2) + 
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
                Math.sin(longDist / 2) * Math.sin(longDist / 2);
		double c =  2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		return 3959 * c;
	}
	
	/**
	 * Static class which holds vertex data information of the graph. Locations are
	 * made up of Latitude and Longitude coordinates.
	 * @author Ray Kinsella
	 *
	 */
	public static class Location{
		private double latitude, longitude;
		
		//Basic Constructor
		public Location(double latitude, double longitude){
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		/**
		 * Getter for Latitude
		 * @return This location objects latitude
		 */
		public double getLatitude(){
			return this.latitude;
			
		}
		
		/**
		 * Getter for Longitude
		 * @return This location objects longitude
		 */
		public double getLongitude(){
			return this.longitude;
			
		}
	}
	
	/**
	 * Static class representation of the edge data for the graph. Made up of a distance or weight field,
	 * and a street name.
	 * 
	 * @author Ray Kinsella
	 *
	 */
	public static class Way implements IWeight{
		private String name;
		private double distance;
		
		//Basic Constructor
		public Way(String name, double distance){
			this.name = name;
			this.distance = distance;
		}
		
		/**
		 * Getter for name
		 * @return Street name of edge
		 */
		public String getName(){
			return this.name;
		}
	
		@Override
		public double getWeight() {
			return this.distance;
		}
	}
}
