package test1Practice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class GraphAllTopSorts{
    int V;  // No. of vertices
    LinkedList<Integer>[] adj;  //Adjacency List
    boolean[] marked;   //Boolean array to store the visited nodes
    List<Integer> list;
    int[] indegree; //integer array to store the indegree of nodes

    //Constructor
    public GraphAllTopSorts(int v) {
        this.V=v;
        this.adj = new LinkedList[v];
        for (int i=0;i<v;i++) {
            adj[i] = new LinkedList<Integer>();
        }
        this.indegree = new int[v];
        this.marked = new boolean[v];
        list = new ArrayList<Integer>();
    }

    // function to add an edge to graph
    public void addEdge(int v, int w){
        adj[v].add(w);
        // increasing inner degree of w by 1
        indegree[w]++;
    }

    // Main recursive function to print all possible topological sorts
    public void alltopologicalSorts() {
        // To indicate whether all topological are found or not
        boolean flag = false;

        for (int w=0;w<V;w++) {

            // If indegree is 0 and not yet visited then
            // only choose that vertex
            if (!marked[w] && indegree[w]==0) {
                marked[w] = true;
                Iterator<Integer> iter = adj[w].listIterator();
                while(iter.hasNext()) {
                    int k = iter.next();
                    indegree[k]--;
                }

                // including in list
                list.add(w);
                alltopologicalSorts();

                // resetting marked, list and indegree for backtracking
                marked[w] = false;
                iter = adj[w].listIterator();
                while(iter.hasNext()) {
                    int k = iter.next();
                    indegree[k]++;
                }
                list.remove(list.indexOf(w));

                flag = true;
            }
        }

        // We reach here if all vertices are visited.
        // So we print the solution here
        if (!flag) {
            for (int w=0;w<V;w++) {
                System.out.print(list.get(w) + " ");
            }
            System.out.print("\n");
        }
    }

    // Driver program to test above functions
    public static void main(String[] args) {
        // Create a graph given in the above diagram
        GraphAllTopSorts g = new GraphAllTopSorts(6);
        g.addEdge(5, 2);
        g.addEdge(5, 0);
        g.addEdge(4, 0);
        g.addEdge(4, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        System.out.println("All Topological sorts");

        g.alltopologicalSorts();
    }
}
