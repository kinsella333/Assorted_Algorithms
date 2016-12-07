package cs311.hw7.graphalgorithms;

import cs311.hw7.graph.Graph;
import cs311.hw7.graph.IGraph;
import cs311.hw7.graph.IGraph.Edge;
import cs311.hw7.graph.IGraph.Vertex;
import cs311.hw8.graphalgorithms.IWeight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;


public class GraphAlgorithms{
	/**
	 * TopologicalSort sorts the vertices in the given DAG into topological order
	 * and returns a list of vertices using the first vertex added as a starting index.
	 * 
	 * @param g Given direct graph to be sorted 
	 * @return List of vertices in sorted order
	 * @throws IllegalArgumentException when graph is not directed, or contains a cycle.
	 */
    public static <V, E> List<Vertex<V>> TopologicalSort(IGraph<V, E> g) throws IllegalArgumentException{
    	//Verify g is directed since Topological Sort must be on a DAG
    	try{
    		checkErrors(g, 0);
    	}catch(Exception e){
    		e.printStackTrace(System.err);
    	}
    	
    	List<List<Vertex<V>>> test = topSort(g, false);
    	return test.get(0);
    }
    
    /**
     * TopologicalSort sorts the vertices in the given DAG into all topological order
	 * combinations and returns a list of the topologically sorted lists.
	 * 
     * @param g Given direct graph to be sorted 
	 * @return List of sorted Lists
	 * @throws IllegalArgumentException when graph is not directed, or contains a cycle.
     */
    public static <V, E> List<List<Vertex<V>>> AllTopologicalSort(IGraph<V, E> g )throws IllegalArgumentException{
    	//Verify g is directed since Topological Sort must be on a DAG
    	try{
    		checkErrors(g, 0);
    	}catch(Exception e){
    		e.printStackTrace(System.err);
    	}
    	List<List<Vertex<V>>> test = topSort(g, true);
    	return test;
    }
    
    /**
     * Uses Kruscal's Algorithm to find the MST of the graph.
     * 
     * @param g the graph to find the mst for.
     * @return MST of graph g
     * @throws IllegalArgumentException when graph is not connected, directed, or has null edge data.
     */
    public static <V, E extends IWeight> IGraph<V, E> Kruscal(IGraph<V, E> g )throws IllegalArgumentException{
    	//Verify g is undirected, connected, and contains no null edge data.
    	try{
    		checkErrors(g, 1);
    	}catch(Exception e){
    		e.printStackTrace(System.err);
    	}
    	
        List<Vertex<V>> vList = g.getVertices();
    	Queue<Edge<E>> sorted = sort(g.getEdges());
    	Graph<V, E> current = new Graph<V, E>();
    	Edge<E> edge;
    	Vertex<V> vertex;
    	ArrayList<ArrayList<Vertex<V>>> subsets = new ArrayList<ArrayList<Vertex<V>>>();
    	ArrayList<String> verticeNames = new ArrayList<String>();
    	int v1,v2;
    	
    	//Iterate through to create subsets lists for each vertex, add vertices to current, and create list of vertice's names.
    	for(int i = 0; i < vList.size(); i++){
    		vertex = vList.get(i);
    		verticeNames.add(vertex.getVertexName());
    		current.addVertex(vertex.getVertexName());
    		
    		ArrayList<Vertex<V>> temp = new ArrayList<Vertex<V>>();
    		temp.add(vertex);
    		subsets.add(temp);
    	}
        
    	//Build MST with total edges = total vertices - 1.
    	for(int i = 0; i < vList.size() - 1; i++){
    		//Pop most minimum edge weight remaining, and store the vertices locations.
    		edge = sorted.remove();
			v1 = verticeNames.indexOf(edge.getVertexName1());
			v2 = verticeNames.indexOf(edge.getVertexName2());
			
			//If subset of vertex is empty, then it has already been added to another subset.
			//So we find which subset it is in and adjust the index accordingly.
			if(subsets.get(v1) == null){
				v1 = findIndex(subsets, vList, v1);
	    	}
			if(subsets.get(v2) == null){
				v2 = findIndex(subsets, vList, v2);
	    	}
			//If two nodes are not in the same index we add the edge to current, and merge the subsets.
			if(v1 != v2){
				current.addEdge(edge.getVertexName1(), edge.getVertexName2(), edge.getEdgeData());
				subsets.get(v1).addAll(subsets.get(v2));
				subsets.set(v2, null);
			}	
    	}
    	return current; 
    }
    
    /**
     * Topological sort recursive preparation function.
     * @param g Given direct graph to be sorted 
     * @return List of sorted Lists
     */
    private static <V, E> List<List<Vertex<V>>> topSort(IGraph<V, E> g , boolean all){
    	List<List<Vertex<V>>> list = new ArrayList<List<Vertex<V>>>();
    	List<Vertex<V>> temp = new ArrayList<Vertex<V>>(), vList = g.getVertices();
    	List<Edge<E>> eList = g.getEdges();
    	List<List<Vertex<V>>> test = new ArrayList<List<Vertex<V>>>();
    	Stack<Vertex<V>> stack = new Stack<Vertex<V>>();
    	
    	int size = vList.size();
    	boolean visited[] = new boolean[size];
    	int degreeArr[] = new int[size];
    	
    	//Assign an in degree value for every node.
    	for(int i = 0; i < eList.size(); i++){
    		degreeArr[vList.indexOf(g.getVertex(eList.get(i).getVertexName2()))]++;
    	}
    	if(all){
    		//Run recursive helper function
        	test = allTopSortHelper(g, temp, visited, degreeArr, size, vList, list);
    	}else{
    		test.add(new ArrayList<Vertex <V>>());
    		for(int i = 0; i < size; i++){
    			if(!visited[i]){
        			topSortHelper(g, stack, visited, size, vList, i); 
    			}
    		}
    		while(!stack.empty()){
    			test.get(0).add(stack.pop());
    		}
    	}
    	return test;
    }
    
    /**
     * Recursive function which solves for all topological sort combinations on a given DAG. 
     * @param g Given direct graph to be sorted 
     * @param tempL Temporary list, holds the sorted order of 1 topological sort.
     * @param visited Boolean array noting when vertices have been visited
     * @param degreeArr The in degree of each vertex held in an array.
     * @param size the total number of Vertices
     * @param vList the list of the vertices in the graph
     * @param listOfLists The final output of all topological sorts of the graph.
     * @return The final output of all topological sorts of the graph.
     */
    private static <V, E> List<List<Vertex<V>>> allTopSortHelper(IGraph<V, E> g, List<Vertex<V>> tempL, boolean[] visited, 
    		int[] degreeArr, int size, List<Vertex<V>> vList, List<List<Vertex<V>>> listOfLists){
    	boolean flag = false;
    	
    	for(int i = 0; i < size; i++){
    		 //If the vertex has not been visited, or the degree in is at 0 we add it to the 
    		 //current sorted list and reduce all of its neighbors by 1 input value, simulating a severing of the edge.
    		 if (!visited[i] && degreeArr[i] == 0){
    			 visited[i] = true;
    			 List<Vertex<V>> nList = g.getNeighbors(vList.get(i).getVertexName());
    			 int temp;
    			 
    			 for(int k = 0; k < nList.size(); k++){
    		    	 temp = vList.indexOf(nList.get(k));
                     degreeArr[temp]--;
                 }
                 tempL.add(vList.get(i));

                 //Recursive call to helper function to evaluate all possible routes and make sure they are included in the result.
                 allTopSortHelper(g, tempL, visited, degreeArr, size, vList, listOfLists);
                 
                 //Remove the evidence that the vertices have been visited in case another ordering follows a similar path. 
                 visited[i] = false;
                 for(int k = 0; k < nList.size(); k++){
    		    	 temp = vList.indexOf(nList.get(k));
                     degreeArr[temp]++;
                 }
                 tempL.remove(tempL.indexOf(vList.get(i)));
                 flag = true;
    		 }
    	}
    	
    	//If we have a fully sorted ordering we do a deep copy in the listOfLists output List and return.
    	if(!flag){
    		listOfLists.add(new ArrayList<Vertex<V>>());
    		for(int i = 0; i < size; i++){
    			listOfLists.get(listOfLists.size() - 1).add(tempL.get(i));
    		}
    		//if(all == 1) all++;
    	}
    	
    	return listOfLists;
    }
    
    /**
     * Recursive function for Single Topological Sort
     * @param g Graph to sort
     * @param stack Used to collect vertices
     * @param visited Boolean array of visited nodes
     * @param size Number of vertices
     * @param vList List of vertices
     * @param i Current index of loop
     */
    private static <V, E> void topSortHelper(IGraph<V, E> g, Stack<Vertex<V>> stack, boolean[] visited, 
    		int size, List<Vertex<V>> vList, int i){
    	visited[i] = true;
    	
    	List<Vertex<V>> nList = g.getNeighbors(vList.get(i).getVertexName());
		int temp;
		 
		for(int k = 0; k < nList.size(); k++){
	    	temp = vList.indexOf(nList.get(k));
            if(!visited[temp]){
            	topSortHelper(g, stack, visited, size, vList, temp);
            }
        }
		stack.push(vList.get(i));
    }
   
    /**
     * Check function to see if graph contains a cycle.
     * @param g Given direct graph to be sorted 
     * @return True if graph contains cycle, false otherwise.
     */
    private static <V, E> boolean checkCycles(IGraph<V, E> g){
    	List<Edge<E>> eList = g.getEdges();
    	for(int i = 0; i < eList.size(); i++){
    		if(eList.get(i).getVertexName1().equals(eList.get(i).getVertexName2())){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Check if graph g is connected via bfs.
     * 
     * @param g Graph to be checked.
     * @return true if connected, false if not.
     */
    private static <V, E> boolean checkConnected(IGraph<V, E> g){
    	Queue<Vertex<V>> q = new LinkedList<Vertex<V>>();
    	List<Vertex<V>> vList = g.getVertices(), nList = new ArrayList<Vertex<V>>();
    	boolean visited[] = new boolean[vList.size()];
    	String temp;
    	Vertex<V> child;
    	int counter = 1;
    	
    	//Add starting vertex to queue and set visited to true.
    	q.add(vList.get(0));
    	visited[0] = true;
    	
    	//While the queue is not empty perform bfs on the children of the current root.
    	while(!q.isEmpty()){
    		temp = q.peek().getVertexName();
    		nList = g.getNeighbors(temp);
    		child = null;
    		
    		//check if children have been visited.
    		for(int i = 0; i < nList.size(); i++){
    			if(!visited[vList.indexOf(nList.get(i))]){
    				child = nList.get(i);
    			}
    		}
    		
    		//If there is a child of the node then add the child to the queue and set visited to true.
    		if(child != null){
    			visited[vList.indexOf(child)] = true;
    			q.add(child);
    			counter++; 
    		}else{
    			q.remove();
    		}
    	}
    	//If counter does not equal the amount of vertices, then we did not visit every node and the graph is not connected.
    	if(counter == vList.size()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * Verify that edge data is not null for any edge in the graph, as that would create a problem when computing the MST.
     * 
     * @param eList List of edges in a tree.
     * @return true if edge data does not contain null, false if otherwise.
     */
    private static <V, E> boolean checkEdgeData(List<Edge<E>> eList){
    	for(int i = 0; i < eList.size(); i++){
    		if(eList.get(i).getEdgeData() == null){
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Compilation of error checks to tidy up the algorithm code.
     * 
     * @param g Graph to perform checks on.
     * @param type Type of checks to perform, currently supports topological sort check(0), and MST checks(1).
     */
    private static <V, E> void checkErrors(IGraph<V, E> g, int type){
    	List<Edge<E>> eList = g.getEdges();
    	switch(type){
    	//Topological sort check
    	case 0:
    		if(!g.isDirectedGraph()){
        		throw new IllegalArgumentException("Graph is not Directed");
        	}
        	if(checkCycles(g)){
        		throw new IllegalArgumentException("Graph Contains a cycle");
        	}
        	return;
        //MST Checks
    	case 1:
    		 if(g.isDirectedGraph()){
    	        throw new IllegalArgumentException("Graph is Directed");
	        }
	        if(!checkConnected(g)){
	        	throw new IllegalArgumentException("Graph is not Connected");
	        }
	        if(!checkEdgeData(eList)){
	        	throw new IllegalArgumentException("Graph contains null edge data");
	        }
	        return;
    	}
    }
    
    /**
     * Takes in a weighted edge list, and sorts the weights from low to high, and returns an ordered queue.
     * 
     * @param eList List of edges from a graph
     * @return Queue that who's root is the min of the list.
     */
    private static <V, E extends IWeight> Queue<Edge<E>> sort(List<Edge<E>> eList){
    	List<Edge<E>> sorted = new ArrayList<Edge<E>>();
    	Queue<Edge<E>> sortedQ = new LinkedList<Edge<E>>();
    	
    	//Add all edges
    	for(int i = 0; i < eList.size(); i++){
    		sorted.add(eList.get(i));
    	}
    	
    	//Sort edges in ascending order
    	Collections.sort(sorted, new Comparator<Edge<E>>() {
    	    @Override
    	    public int compare(Edge<E> c1, Edge<E> c2) {
    	        return Double.compare(c1.getEdgeData().getWeight(), c2.getEdgeData().getWeight());
    	    }
    	});
    	
    	//Add edges to queue
    	for(int i = 0; i < sorted.size(); i++){
    		sortedQ.add(sorted.get(i));
    	}
    	return sortedQ;
    }
    
    /**
     * Finds index of the vertex in the subsets.
     * 
     * @param subsets The subsets containing the vertex lists.
     * @param vList List of vertices. 
     * @param v1 Initial index of the vertex.
     * @return The index of the subset where it is located.
     */
    private static <V> int findIndex(ArrayList<ArrayList<Vertex<V>>> subsets, List<Vertex<V>> vList, int v1){
    	for(int j = 0; j < subsets.size(); j++){
    		if(subsets.get(j) != null){
    			if(subsets.get(j).contains(vList.get(v1))){
    				return j;
    			}
    		}
		}
    	return -1;
    }
}