package cs311.hw8.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph<V,E> implements IGraph<V,E>{
	
	private List<Edge<E>> edges = new ArrayList<Edge<E>>(1);
	private List<Vertex<V>> vertices = new ArrayList<Vertex<V>>(1);
	private boolean directed = false;

	/**
     * Set the graph to be a directed graph.  Edge (x, y) is different than edge (y, x)
     */
	@Override
	public void setDirectedGraph() {
		this.directed = true;
	}

	/**
     * Set the graph to be an undirected graph.  Edge (x, y) is in the graph
     * if and only if edge (y, x) is in the graph.  Note that when implementing this
     * and there are already edges defined in the graph, care must be taken to 
     * resolve conflicts and inconsistencies in the overall implementation.
     */
	@Override
	public void setUndirectedGraph() {
		this.directed = false;
		Edge<E> e1, e2;
		
		for(int i = 0; i < this.edges.size(); i++){
			e1 = this.edges.get(i);
			for(int k = 0; k < this.edges.size(); k++){
				e2 = this.edges.get(k);
				if(e1.getVertexName1().equals(e2.getVertexName2()) && e1.getVertexName2().equals(e2.getVertexName1())){
					//The most recent edge added gets priority on edge data. 
					//I chose to do this following Jim's comment on the discussion board under the Changing between directed and undirected thread.
					e1 = new Edge<E>(e2.getVertexName2(), e2.getVertexName1(), e2.getEdgeData());
					break;
				}
			}
		}
	}

	 /**
     * @return true if the graph is directed.
     */
	@Override
	public boolean isDirectedGraph() {
		return this.directed;
	}

	/**
     * Adds a vertex to the graph with name given by the vertexName.  vertexNames,
     * must be unique in the graph.
     * 
     * @param vertexName The unique name of the vertex.
     * @throws DuplicateVertexException 
     */
	@Override
	public void addVertex(String vertexName) throws DuplicateVertexException {
		//If vertex of vertexName not already in list, add new vertex.
		if(this.getVertex(vertexName) != null){
			throw new DuplicateVertexException();
		}else{
			this.vertices.add(new Vertex<V>(vertexName, null));
		}
	}

	 /**
     * Adds a vertex to the graph with name given by the vertexName.  vertexNames,
     * must be unique in the graph.  The vertexData of generic type is associated with
     * this vertex.
     * 
     * @param vertexName
     * @param vertexData
     * @throws DuplicateVertexException 
     */
	@Override
	public void addVertex(String vertexName, V vertexData) throws DuplicateVertexException {
		//If vertex of vertexName not already in list, add new vertex.
		if(this.getVertex(vertexName) != null){
			throw new DuplicateVertexException();
		}else{
			this.vertices.add(new Vertex<V>(vertexName, vertexData));
		}
	}

	/**
     * Adds an edge to the graph by specifying the two vertices that comprise the 
     * edge.  If the graph is undirected then edge (x, y) or edge (y, x) may be used
     * to add the single edge.  If the graph is undirected and edge (x,y) is added
     * followed by a subsequent edge (y, x), the later add would throw a 
     * DuplicateEdgeException.
     * 
     * @param vertex1 The first vertex in the edge.
     * @param vertex2 The second vertex in the edge.
     * 
     * @throws DuplicateEdgeException
     * @throws NoSuchVertexException 
     */
	@Override
	public void addEdge(String vertex1, String vertex2)
			throws DuplicateEdgeException, NoSuchVertexException {
		//Verify that Vertices exist in graph
		if(this.getVertex(vertex1) == null || this.getVertex(vertex2) == null){
			throw new NoSuchVertexException();
		}
		
		if(this.isDirectedGraph()){
			//If directed graph only check and add edge in one direction
			if(this.getEdge(vertex1, vertex2) != null){
				throw new DuplicateEdgeException();
			}else{
				edges.add(new Edge<E>(vertex1, vertex2, null));
			}
		}else{
			//Else if undirected, check for both directions and add the new edge.
			if(this.getEdge(vertex1, vertex2) != null || this.getEdge(vertex2, vertex1) != null){
				throw new DuplicateEdgeException();
			}else{
				edges.add(new Edge<E>(vertex1, vertex2, null));
			}
		}
	}

	/**
     * Adds an edge to the graph by specifying the two vertices that comprise the 
     * edge.  If the graph is undirected then edge (x, y) or edge (y, x) may be used
     * to add the single edge.  If the graph is undirected and edge (x,y) is added
     * followed by a subsequent edge (y, x), the later add would throw a 
     * DuplicateEdgeException.  The edgeDaa parameter is used to associate generic
     * edge data with the edge.
     * 
     * @param vertex1 The first vertex in the edge.
     * @param vertex2 The second vertex in the edge.
     * @param edgeData The generic edge data.
     * 
     * @throws DuplicateEdgeException
     * @throws NoSuchVertexException 
     */
	@Override
	public void addEdge(String vertex1, String vertex2, E edgeData)
			throws DuplicateEdgeException, NoSuchVertexException {
		
		//Verify that Vertices exist in graph
		if(!(this.getVertex(vertex1) != null || this.getVertex(vertex2) != null)){
			throw new NoSuchVertexException();
		}
		
		if(this.isDirectedGraph()){
			//If directed graph only check and add edge in one direction
			if(this.getEdge(vertex1, vertex2) != null){
				throw new DuplicateEdgeException();
			}else{
				edges.add(new Edge<E>(vertex1, vertex2, edgeData));
			}
		}else{
			//Else if undirected, check for both directions and add the new edge.
			if(this.getEdge(vertex1, vertex2) != null || this.getEdge(vertex2, vertex1) != null){
				throw new DuplicateEdgeException();
			}else{
				edges.add(new Edge<E>(vertex1, vertex2, edgeData));
			}
		}
	}

	/**
     * Returns the generic vertex data associated with the specified vertex.  If no
     * vertex data is associated with the vertex, then null is returned.
     * 
     * @param vertexName  Name of vertex to get data for
     * @return The generic vertex data
     * @throws NoSuchVertexException 
     */
	@Override
	public V getVertexData(String vertexName) throws NoSuchVertexException {
		Vertex<V> v = this.getVertex(vertexName);
		
		//If v is contained in the vertices list, then return the data stored there, which is already
		//set to null if not available.
		if(v != null){
			return v.getVertexData();
		}else{
			throw new NoSuchVertexException();
		}
	}

	 /**
     * Sets the generic vertex data of the specified vertex.
     * 
     * @param vertexName The name of the vertex.
     * @param vertexData The generic vertex data.
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException 
     */
	@Override
	public void setVertexData(String vertexName, V vertexData) throws NoSuchVertexException {
		Vertex<V> v = this.getVertex(vertexName);
		
		//If v is in the list of vertices we remove it and add a new vertex with the specified data.
		if(v != null){
			this.vertices.remove(v);
			this.vertices.add(new Vertex<V>(vertexName, vertexData));
		}else{
			throw new NoSuchVertexException();
		}
	}

	/**
     * Returns the generic edge data associated with the specified edge.  If no
     * edge data is associated with the edge, then null is returned.
     * 
     * @param vertex1 Vertex one of the edge.
     * @param vertex2 Vertex two of the edge.
     * @return The generic edge data.
     * @throws NoSuchVertexException
     * @throws NoSuchEdgeException 
     */
	@Override
	public E getEdgeData(String vertex1, String vertex2)
			throws NoSuchVertexException, NoSuchEdgeException {
		
		//Verify that Vertices exist in graph
		if(!(this.getVertex(vertex1) != null || this.getVertex(vertex2) != null)){
			throw new NoSuchVertexException();
		}
		Edge<E> e = this.getEdge(vertex1, vertex2);
		
		//If e is in the list of edges we return the data.
		if(e != null){
			return e.getEdgeData();
		}else{
			throw new NoSuchEdgeException();
		}
	}

	/**
     * Sets the generic edge data of the specified edge.
     * 
     * @param vertex1  Vertex one of the edge.
     * @param vertex2  Vertex two of the edge.
     * 
     * @param edgeData The generic edge data.
     * 
     * @throws cs311.hw6.graph.IGraph.NoSuchVertexException
     * @throws cs311.hw6.graph.IGraph.NoSuchEdgeException 
     */
	@Override
	public void setEdgeData(String vertex1, String vertex2, E edgeData)
			throws NoSuchVertexException, NoSuchEdgeException {
		
		//Verify that Vertices exist in graph
		if(!(this.getVertex(vertex1) != null || this.getVertex(vertex2) != null)){
			throw new NoSuchVertexException();
		}
		Edge<E> e = this.getEdge(vertex1, vertex2);
		
		//If e is in the list of edges we remove it and add a new edge with the specified data.
		if(e != null){
			this.edges.remove(e);
			this.edges.add(new Edge<E>(vertex1, vertex2, edgeData));
		}else{
			throw new NoSuchEdgeException();
		}
		
	}

	/**
     * Returns an encapsulated Vertex data type based on the vertex name
     * 
     * @param VertexName The name of the vertex.
     * @return The encapsulated vertex. 
     */
	@Override
	public Vertex<V> getVertex(String VertexName) {
		Vertex<V> v;
		
		//If we iterate through all vertices and find it, return v, else return null
		for(int i = 0; i < this.vertices.size(); i++){
			v = this.vertices.get(i);
			if(v.getVertexName().equals(VertexName)){
				return v;
			}
		}
		return null;
	}

	 /**
     * Returns an encapsulated Edge data type based on the specified edge.
     * 
     * @param vertexName1 Vertex one of edge.
     * @param vertexName2 Vertex two of edge.
     * @return Encapsulated edge.
     */
	@Override
	public Edge<E> getEdge(String vertexName1, String vertexName2) {
		Edge<E> e;
		
		if(this.isDirectedGraph()){
			//If we iterate through all edges and find it, return e, else return null
			for(int i = 0; i < this.edges.size(); i++){
				e = this.edges.get(i);
				if(e.getVertexName1().equals(vertexName1) && e.getVertexName2().equals(vertexName2)){
					return e;
				}
			}
		}
		//Be sure to return a,b or b,a since they are equivalent in undirected
		else{
			//If we iterate through all edges and find it, return e, else return null
			for(int i = 0; i < this.edges.size(); i++){
				e = this.edges.get(i);
				if((e.getVertexName1().equals(vertexName1) && e.getVertexName2().equals(vertexName2)) || (e.getVertexName1().equals(vertexName2) && e.getVertexName2().equals(vertexName1))){
					return e;
				}
			}
		}
		return null;
	}

	 /**
     * Returns a list of all the vertices in the graph.
     * @return The List<Vertex> of vertices.
     */
	@Override
	public List<Vertex<V>> getVertices() {
		return this.vertices;
	}

	/**
     * Returns all the edges in the graph.
     * @return The List<Edge<E>> of edges. 
     */
	@Override
	public List<Edge<E>> getEdges() {
		return this.edges;
	}

	/**
     * Returns all the neighbors of a specified vertex.
     * 
     * @param vertex The vertex to return neighbors for.
     * @return The list of vertices that are the neighbors of the specified vertex.
     */
	@Override
	public List<Vertex<V>> getNeighbors(String vertex) {
		List<Vertex<V>> neighbors = new ArrayList<Vertex<V>>();
		String v1, v2;
		Edge<E> e;
		
		if(this.getVertex(vertex) != null){	
			//For each edge evaluate if it connects something to vertex and if it has already recorded it
			//If we haven't added it then add it.
			if(this.isDirectedGraph()){
				for(int i = 0; i < this.edges.size(); i++){
					e = this.edges.get(i);
					v1 = e.getVertexName1();
					v2 = e.getVertexName2();
					if(v1.equals(vertex) && !neighbors.contains(v2)){
						neighbors.add(this.getVertex(v2));
					}
				}
				return neighbors;
			}else{
				for(int i = 0; i < this.edges.size(); i++){
					e = this.edges.get(i);
					v1 = e.getVertexName1();
					v2 = e.getVertexName2();
					if(v1.equals(vertex) && !neighbors.contains(v2)){
						neighbors.add(this.getVertex(v2));
					}else if(v2.equals(vertex) && !neighbors.contains(v1)){
						neighbors.add(this.getVertex(v1));
					}
				}
				return neighbors;
			}
		}else{
			throw new NoSuchVertexException();
		}
	}

}
