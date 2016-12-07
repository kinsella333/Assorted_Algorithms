package cs311.hw7.graphalgorithms;

import java.util.ArrayList;
import java.util.List;

import cs311.hw7.graph.Graph;
import cs311.hw7.graph.IGraph.Edge;
import cs311.hw7.graph.IGraph.NoSuchEdgeException;
import cs311.hw7.graph.IGraph.NoSuchVertexException;
import cs311.hw7.graph.IGraph.Vertex;

public class GraphTesting<V,E>{

	public static void main(String[] args){
		Graph<String,Weight> test = new Graph<String,Weight>();
		//test.setDirectedGraph();
		
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
		
		//Graph<String,Weight> kruscal = (Graph<String, Weight>) GraphAlgorithms.Kruscal(test);
		
		
		/*List<List<Vertex<String>>> topList = GraphAlgorithms.AllTopologicalSort(test);
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
		}*/
		
		
		/*//test.setDirectedGraph();
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
		System.out.print("]\n");*/
	}

}
