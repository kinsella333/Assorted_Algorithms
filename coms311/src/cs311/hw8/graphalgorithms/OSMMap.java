package cs311.hw8.graphalgorithms;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cs311.hw8.graph.JimGraph;
import cs311.hw8.graph.IGraph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;
import cs311.hw8.graphalgorithms.OSMMap.Way;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

public class OSMMap{
	
	public IGraph<Location, Way> map;
	
	public <V extends Location, E extends Way> OSMMap(){
		IGraph<V, E> map = new JimGraph<V, E>();
		map.setDirectedGraph();
	}
	
	public void parse(String fileName){
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
			e1.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		doc.getDocumentElement().normalize();
		NodeList locations = doc.getElementsByTagName("node");
		NodeList ways = doc.getElementsByTagName("way");
		int wLength = ways.getLength();
		int locLength = locations.getLength();
		
		for (int temp = 0; temp < locLength; temp++) {
			Node nNode = locations.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               Location v = new Location(new Double(eElement.getAttribute("lat")), new Double(eElement.getAttribute("lon")));
	               map.addVertex(eElement.getAttribute("id"), v);
			}
		}
		for (int temp = 0; temp < wLength; temp++) {
			Node nNode = ways.item(temp);
			boolean nameB = false, hwB = false, oneway = false;
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               NodeList ks = eElement.getElementsByTagName("tag");
	               String name = "";
	               
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
	               
	               if(hwB && nameB){
	            	   NodeList nds = eElement.getElementsByTagName("nd");
	            	   String vertices[] = new String[nds.getLength()];
	            
	            	   for (int j = 0; j < nds.getLength(); j++) {
	            		   Node aNode = nds.item(j);
	            		   if(aNode.getNodeType() == Node.ELEMENT_NODE){
	            			   Element aElement = (Element) aNode;
	            			   vertices[j] = aElement.getAttribute("ref"); 
	            		   }
	            	   }
	            	   
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
	}
	
	
	public <E extends Way> double TotalDistance(){
		double dist = 0;
		System.out.println(map.getEdges().size());
		List<Edge<Way>> t1 = map.getEdges();
		for(int i = 0; i < t1.size(); i++){
			dist += t1.get(i).getEdgeData().getDistance();
		}
		return dist/2;
		
	}
	
	public int closestRoad(){
		
		return 0;
		
	}

	public List<String> shortestRoute(String v1, String v2){
		List<Edge<Way>> eList = GraphAlgorithms.ShortestPath(this.map, v1, v2);
		List<String> sList = new ArrayList<String>();
		int size = eList.size();
		for(int i = 0; i < size; i++){
			sList.add(eList.get(i).getEdgeData().getName());
		}
		return sList;
		
	}
	
	//https://bigdatanerd.wordpress.com/2011/11/03/java-implementation-of-haversine-formula-for-distance-calculation-between-two-points/
	private double getDistance(double lat1, double long1, double lat2, double long2){
		double dist;
		double latDist = Math.toRadians(lat2-lat1);
		double longDist = Math.toRadians(long2-long1);
		
		double a = Math.sin(latDist / 2) * Math.sin(latDist / 2) + 
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
                Math.sin(longDist / 2) * Math.sin(longDist / 2);
		double c =  2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		return 3959 * c;
	}
	
	public static class Location{
		private double latitude, longitude;
		
		public Location(double latitude, double longitude){
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		public double getLatitude(){
			return this.latitude;
			
		}
		
		public double getLongitude(){
			return this.longitude;
			
		}
	}
	
	public static class Way{
		private String name;
		private double distance;
		
		public Way(String name, double distance){
			this.name = name;
			this.distance = distance;
		}
		
		public String getName(){
			return this.name;
		}
		
		public double getDistance(){
			return this.distance;
		}
	}
}
