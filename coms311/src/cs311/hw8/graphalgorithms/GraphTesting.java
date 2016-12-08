package cs311.hw8.graphalgorithms;

import java.util.List;

import cs311.hw8.graph.Graph;
import cs311.hw8.graph.IGraph.Edge;
import cs311.hw8.graph.IGraph.Vertex;
import cs311.hw8.graphalgorithms.OSMMap.Location;
import cs311.hw8.graphalgorithms.OSMMap.Way;

public class GraphTesting<V,E extends IWeight>{

	/*public static void main(String[] args){
		Graph<String,Weight> test = new Graph<String,Weight>();
		test.setDirectedGraph();
		
		test.addVertex("0");
		test.addVertex("1");
		test.addVertex("2");
		test.addVertex("3");
		test.addVertex("4");
		test.addVertex("5");
		test.addVertex("6");
		test.addVertex("7");
		test.addVertex("8");

		test.addEdge("0", "1", new Weight(4));
		test.addEdge("0", "7", new Weight(8));
		test.addEdge("1", "2", new Weight(8));
		test.addEdge("1", "7", new Weight(11));
		test.addEdge("7", "6", new Weight(1));
		test.addEdge("7", "8", new Weight(7));
		test.addEdge("6", "8", new Weight(6));
		test.addEdge("6", "5", new Weight(2));
		test.addEdge("2", "8", new Weight(2));
		test.addEdge("2", "3", new Weight(7));
		test.addEdge("2", "5", new Weight(4));
		test.addEdge("3", "5", new Weight(14));
		test.addEdge("3", "4", new Weight(9));
		test.addEdge("5", "4", new Weight(10));
		
		OSMMap t = new OSMMap();
		t.parse("AmesMap.txt");
		String id1 = t.ClosestRoad(new OSMMap.Location(42.023354, -93.668641));
		//System.out.println(id1);
		//List<Vertex<Location>> t1 = t.map.getNeighbors(id1);
		//List<Edge<Way>> eList = t.map.getEdges();
		//System.out.println(t.TotalDistance());
		List<String> t1 = t.ShortestRoute(new OSMMap.Location(42.023354, -93.668641),
			new OSMMap.Location(42.028598, -93.651174));
		for(int i = 0; i < eList.size(); i++){
			Edge<Way> e = eList.get(i);
			if(e.getEdgeData().getName().equals("Marshall Avenue")){
				System.out.println(e.getEdgeData().getName());
			}
		}
		
		List<String> streets = t.StreetRoute(t1);
		for(int i = 0; i < streets.size(); i++){
			//Vertex<Location> v = t1.get(i);
			//String a = t.ClosestRoad(v.getVertexData());
			//Location loc = (Location) t.map.getVertex(a).getVertexData();
			//System.out.println("Lat: " + loc.getLatitude() + " Lon: " + loc.getLongitude());
			String road = streets.get(i);
			System.out.println("Road: " + road);
		}
		
		
		
		 Graph<Integer, Weight> g = new Graph();
	        g.setDirectedGraph();
	        g.addVertex("A");
	        g.addVertex("B");
	        g.addVertex("C");
	        g.addVertex("D");
	        g.addVertex("E");
	        g.addVertex("F");
	        g.addVertex("G");
	        g.addVertex("H");
	        g.addVertex("I");
	        g.addVertex("J");
	        g.addVertex("K");
	        g.addVertex("L");
	        g.addVertex("M");
	        g.addVertex("N");
	        g.addVertex("O");
	        g.addVertex("P");
	        g.addVertex("Q");
	        g.addVertex("R");
	        g.addVertex("S");
	        g.addVertex("T");
	        g.addVertex("Z");

	        g.addEdge("A", "B", new Weight(2));
	        g.addEdge("A", "C", new Weight(4));
	        g.addEdge("A", "D", new  Weight(1));
	        g.addEdge("B", "C", new  Weight(3));
	        g.addEdge("B", "E", new  Weight(1));
	        g.addEdge("C", "E", new  Weight(2));
	        g.addEdge("C", "F", new  Weight(2));
	        g.addEdge("D", "F", new  Weight(5));
	        g.addEdge("D", "G", new  Weight(4));
	        g.addEdge("E", "H", new  Weight(3));
	        g.addEdge("F", "H", new  Weight(3));
	        g.addEdge("F", "I", new  Weight(2));
	        g.addEdge("F", "J", new  Weight(4));
	        g.addEdge("F", "G", new  Weight(3));
	        g.addEdge("G", "K", new  Weight(2));
	        g.addEdge("H", "O", new  Weight(8));
	        g.addEdge("H", "L", new  Weight(1));
	        g.addEdge("I", "L", new  Weight(3));
	        g.addEdge("I", "M", new  Weight(2));
	        g.addEdge("I", "J", new  Weight(3));
	        g.addEdge("J", "M", new  Weight(6));
	        g.addEdge("J", "N", new  Weight(3));
	        g.addEdge("J", "K", new  Weight(6));
	        g.addEdge("K", "N", new  Weight(4));
	        g.addEdge("K", "R", new  Weight(2));
	        g.addEdge("L", "O", new  Weight(6));
	        g.addEdge("L", "M", new  Weight(3));
	        g.addEdge("M", "O", new  Weight(4));
	        g.addEdge("M", "P", new  Weight(2));
	        g.addEdge("M", "N", new  Weight(5));
	        g.addEdge("N", "Q", new  Weight(2));
	        g.addEdge("N", "R", new  Weight(1));
	        g.addEdge("O", "S", new  Weight(6));
	        g.addEdge("O", "P", new  Weight(2));
	        g.addEdge("P", "S", new  Weight(2));
	        g.addEdge("P", "T", new  Weight(1));
	        g.addEdge("P", "Q", new  Weight(1));
	        g.addEdge("Q", "T", new  Weight(3));
	        g.addEdge("S", "Z", new  Weight(2));
	        g.addEdge("T", "Z", new  Weight(1));
	        
	        List<Edge<Weight>> eList = GraphAlgorithms.ShortestPath(g, "A", "Z");
	        double w = 0;
	        for(int i = 0; i < eList.size(); i++){
				Edge<Weight> e = eList.get(i);
				System.out.println("(" + e.getVertexName1() + ", " + e.getVertexName2()
				+ ") Weight: " + e.getEdgeData().getWeight());
				w += e.getEdgeData().getWeight();
			}
	        System.out.println("Total: " + w);
	        
		List<Edge<Weight>> eList = GraphAlgorithms.ShortestPath(test, "0", "8");
		for(int i = 0; i < eList.size(); i++){
			Edge<Weight> e = eList.get(i);
			System.out.println("V1: " + e.getVertexName1() + " V2: " + e.getVertexName2()
			+ " EW: " + e.getEdgeData().getWeight());
		}
		
		List<List<Vertex<String>>> topList = GraphAlgorithms.AllTopologicalSort(test);
		List<Vertex<String>> topS = GraphAlgorithms.TopologicalSort(test);
		
		for(int i = 0; i < topS.size(); i++){
			System.out.print(topS.get(i).getVertexName() + ", ");
		}
		System.out.println();
		for(int i = 0; i < topList.size(); i++){
			for(int k = 0; k < test.getVertices().size(); k++){
				System.out.print(topList.get(i).get(k).getVertexName() + ", ");
			}
			System.out.println();
		}
		
		
		//test.setDirectedGraph();
		//test.addEdge("FUCK", "Hi");
		try {
			test.setEdgeData("Hi", "FUCK", 2);
		} catch (NoSuchVertexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchEdgeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Vertex<String>> vList = test.getVertices();
		List<Edge<Integer>> eList = test.getEdges();
		
		List<Vertex<String>> nList = test.getNeighbors("Hi");
		
		System.out.print("[");
		for(int i = 0; i < vList.size(); i++){
			System.out.print(vList.get(i).getVertexName() + ", ");
		}
		System.out.print("]\n[");
		for(int i = 0; i < eList.size(); i++){
			System.out.print("(" + eList.get(i).getVertexName1() + "<" + eList.get(i).getEdgeData() + ">, " + eList.get(i).getVertexName2() + "), ");
		}
		System.out.print("]\n");
		
		System.out.print("[");
		for(int i = 0; i < nList.size(); i++){
			System.out.print(nList.get(i).getVertexName() + ", ");
		}
		System.out.print("]\n");
	}
*/
}
