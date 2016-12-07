package cs311.hw8.graphalgorithms;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cs311.hw8.graph.Graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.*;

public class OSMMap {
	
	private Graph<Location, Way> map = new Graph<Location, Way>();
	
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
		double sTime = System.currentTimeMillis();
		
		for (int temp = 0; temp < locLength; temp++) {
			Node nNode = locations.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               map.addVertex(eElement.getAttribute("id"), new Location(new Double(eElement.getAttribute("lat")), new Double(eElement.getAttribute("lon"))));
			}
			
			if((System.currentTimeMillis() - sTime) % 5000 == 0){
				System.out.println("Current Node Percentage: " + Math.round(((double)temp/(double)locLength)*10000.0)/100.0 + "%");
			}
		}
		
		for (int temp = 0; temp < wLength; temp++) {
			Node nNode = ways.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	               Element eElement = (Element) nNode;
	               NodeList ks = eElement.getElementsByTagName("tag");
	               String hw = "", name = "";
	               
	               for (int j = 0; j < ks.getLength(); j++) {
            		   Node aNode = ks.item(j);
            		   if(aNode.getNodeType() == Node.ELEMENT_NODE){
            			   Element aElement = (Element) aNode;
            			   if(aElement.getAttribute("k").equals("highway")){
            				   hw = aElement.getAttribute("k");
            			   }
            			   if(aElement.getAttribute("k").equals("name")){
            				   name = aElement.getAttribute("k");
            			   }
            		   }
	               }
	               
	               if(!hw.equals("") && !name.equals("")){
	            	   NodeList nds = eElement.getElementsByTagName("nd");
	            	   boolean oneway = false;
	            	   String vertices[] = new String[nds.getLength()];
	            	   
	            	   if(eElement.getAttribute("k").equals("oneway") && eElement.getAttribute("v").equals("yes")){
	            		   oneway = true;
	            	   }
	            	   
	            	   for (int j = 0; j < nds.getLength(); j++) {
	            		   Node aNode = nds.item(j);
	            		   if(aNode.getNodeType() == Node.ELEMENT_NODE){
	            			   Element aElement = (Element) aNode;
	            			   vertices[j] = aElement.getAttribute("ref"); 
	            		   }
	            		   
	            	   }
	            	   
	            	   for (int j = 0; j < vertices.length; j++) {
	            		   if(oneway){
	            			   if(j != vertices.length - 1){
	            				   map.addEdge(vertices[j], vertices[j+1]);   
	            			   }
	            		   }else{
	            			   if(j != vertices.length - 1){
	            				   map.addEdge(vertices[j], vertices[j+1]);  
	            				   map.addEdge(vertices[j+1], vertices[j]);
	            			   } 
	            		   }
	            		   
	            	   }
	            	   
	               }
			}
			if((System.currentTimeMillis() - sTime) % 5000 == 0){
				System.out.println("Current Way Percentage: " + Math.round(((double)temp/(double)wLength)*10000.0)/10.0 + "%");
			}
		}
	}
	
	
	private double totalDistance(){
		return 0;
		
	}
	
	public int closestRoad(){
		return 0;
		
	}

	public List<String> shortestRoute(){
		return null;
		
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
		
		public Way(String name){
			
		}
	}
}
