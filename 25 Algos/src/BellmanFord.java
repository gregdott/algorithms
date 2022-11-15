import java.util.*;

/*
 * Author: Gregory Dott
 * 31-10-2022
 * 
 * From Wikipedia:
 * ===========================================================================================================================
 * The Bellman–Ford algorithm is an algorithm that computes shortest paths from a single source vertex to all of the other 
 * vertices in a weighted digraph. It is slower than Dijkstra's algorithm for the same problem, but more versatile, as it 
 * is capable of handling graphs in which some of the edge weights are negative numbers. The algorithm was first proposed 
 * by Alfonso Shimbel (1955), but is instead named after Richard Bellman and Lester Ford Jr., who published it in 1958 and 
 * 1956, respectively. Edward F. Moore also published a variation of the algorithm in 1959, and for this reason it is also 
 * sometimes called the Bellman–Ford–Moore algorithm.
 * ===========================================================================================================================
 * 
 * ^^^ The Wikipedia description's second sentence might be difficult to understand. As far as I can tell, what they are saying there is
 * that its capability of handling graphs with negative edge weights is the reason that it is slower than Dijkstra's Algorithm.
 * 
 * Initialisation and basic data structures are pretty much identical to what is used in the implementation of Dijkstra's Algorithm.
 * This algorithm is in fact so similar to Dijkstra's that much of the code is shared between the two implementations.
 * 
 * ------------------------------------------------------------------------------------------------------------------------------------------
 * If you want to get the shortest paths between all vertices in the graph, just loop through vertices and call calculateShortestPaths()
 * with the current vertex as startVertex. Also, make sure when changing edge data that the numVertices value correctly reflects the
 * number of vertices in the graph.
 * ------------------------------------------------------------------------------------------------------------------------------------------
 */

public class BellmanFord {
    public static void main(String args[]) {
        //int[][] edges = {{0, 1, 10}, {0, 4, 3}, {1, 2, 2}, {1, 4, -4}, {2, 3, 9}, {3, 2, 7}, {4, 1, 1}, {4, 2, 8}, {4, 3, 2}}; // for negative cycle testing
        //int[][] edges = {{0, 1, -1}, {0, 2, 4}, {1, 2, 3}, {1, 3, 2}, {1, 4, 2}, {3, 2, 5}, {3, 1, 1}, {4, 3, -3 }};
        int[][] edges = {{0, 1, 10}, {0, 4, 3}, {1, 2, 2}, {1, 4, 4}, {2, 3, 9}, {3, 2, 7}, {4, 1, 1}, {4, 2, 8}, {4, 3, 2}};

        int numVertices = 5; // the number of vertices in the graph (we can't just assume that the graph is connected so we can not simply deduce this from the edges)
        int startVertex = 0; // the vertex from which we are calculating the shortest paths to all the other vertices
        Graph wg = new Graph(edges, numVertices, true, true); // create new directed, weighted graph (true, true)
        int[] dist = new int[numVertices]; // array storing distances for each vertex to startVertex. This gets progressively refined during execution.
        int[] prev = new int[numVertices]; // array storing the previous vertex along the path to the vertex at i corresponding with the current shortest path

        boolean noNegativeCycles = calculateShortestPaths(dist, prev, wg, startVertex);

        if (noNegativeCycles) {
            printShortestPaths(startVertex, dist, prev);
        } else {
            Pr.x("Graph contains a negative cycle!");
        }
    }

     /**
     * calculateShortestPath - calculates the shortest path from the startVertex to all other vertices in the graph
     * using the Bellman-Ford Algorithm
     * 
     * @param dist distances from startVertex (while calculating this stores current min distance from start to i)
     * @param prev tree structure storing paths to startVertex
     * @param wg weighted graph object
     * @param startVertex the vertex we begin the search from
     * @throws Exception
     */
    private static boolean calculateShortestPaths(int[] dist, int[] prev, Graph wg, int startVertex) {
        
        initDataStructures(dist, prev, startVertex); // initialise the data structures we are using
        List<Edge> edgeList = wg.getEdgeList();
        // iterate n-1 times, where n is the number of nodes. This ensures that we investigate all possible routes from startVertex to the other vertices
        for (int i = 0; i < dist.length - 1; i++) { 
            for (Edge edge: edgeList) {
                // If the distance of the path along the current edge is less than what we have calculated along another path to the current dest vertex
                if (dist[edge.getSource()] + edge.getWeight() < dist[edge.getDest()]) { 
                    dist[edge.getDest()] = dist[edge.getSource()] + edge.getWeight();
                    prev[edge.getDest()] = edge.getSource(); // record the new path to the dest vertex
                }
            }
        }

        // Check for negative cycles in graph. By virtue of having executed the previous for-loop [dist.length -1] times,
        // if any of our paths end up shorter, we must have a negative cycle in the graph.
        for (Edge edge: edgeList) {
            if (dist[edge.getSource()] + edge.getWeight() < dist[edge.getDest()]) {
                return false;
            }
        }

        return true;
    }

    /**
     * initDataStructures - initialise the data structures necessary for calculating the shortest paths
     * 
     * @param dist distances from startVertex (while calculating this stores current min distance from start to i)
     * @param prev tree structure storing paths to startVertex
     * @param unvisited list of unvisited vertices
     * @param startVertex the vertex we begin the search from
     */
    private static void initDataStructures(int[] dist, int[] prev, int startVertex) {
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE; // initialise to largest possible value
            prev[i] = -1;
        }
        dist[startVertex] = 0;
    }

    /**
     * printShortestPaths - after calculating the shortest paths from the source vertex to all other nodes,
     * print the distance and the reconstructed paths
     * 
     * @param startVertex the vertex our search began from
     * @param dist distances from startVertex
     * @param prev tree structure storing paths to startVertex
     */
    private static void printShortestPaths(int startVertex, int[] dist, int[] prev) {
        for (int i = 0; i < dist.length; i++) {
            if (i == startVertex) continue;
            Pr.x("Path from " + startVertex + " to " + i);
            Pr.x("Distance: " + dist[i]);

            List<Integer> path = reconstructPath(startVertex, i, prev);
            int[] pathReversed = new int[path.size()];
            int count = 0;
            for (int j = path.size() - 1; j >= 0; j--) { // this just reverses the path so we can see it in the order that the nodes were visited
                pathReversed[count] = path.get(j);
                count++;
            }
            Pr.x(Arrays.toString(pathReversed));
        }
    }

    /**
     * reconstructPath - reconstruct the shortest path from startVertex to dest vertex
     * 
     * @param startVertex the vertex our search began from
     * @param dest destination vertex
     * @param prev tree structure storing paths to startVertex
     * @return
     */
    private static List<Integer> reconstructPath(int startVertex, int dest, int[] prev) {
        int currentVertex = dest;
        List<Integer> path = new ArrayList<Integer>();        
        path.add(currentVertex);
        
        while(currentVertex != startVertex) { // while we have not found our way back to the start vertex
            currentVertex = prev[currentVertex]; // traverse up the tree to the previous vertex from the current one
            path.add(currentVertex);
        }

        return path;
    }
}
