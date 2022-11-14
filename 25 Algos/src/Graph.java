import java.util.*;


/*
 * Author: Gregory Dott
 * 27-10-2022
 * 
 * A simple Graph class that can cater for essentially 4 different types of graphs:
 * 
 * undirected & unweighted
 * undirected & weighted
 * directed & unweighted
 * directed & weighted
 * 
 */

public class Graph {
    private List<Edge> edgeList; // list containing the edges in the graph

    // list containing the edges of the minimum spanning tree. This gets progressively updated, 
    // so there could be points in time when it is incomplete. Is this ok in terms of naming then?
    private List<Edge> minSpanningTree; 
    private boolean isDirected;
    private boolean isWeighted;
    private List<Integer> nodeList; // Just a list of numbered nodes.
    private List<List<Integer>> adjList; // adjacency list
    private List<Edge> orderedByWeight; // contains the same edges as in edgeList, but it is ordered according to the weight of the edge in ascending order
    private int numNodes; // number of nodes in the graph

    private int[][] edgeWeights; // quick way for getting edge weights. Just an nxn matrix where n is the number of nodes.

    /**
     * WeightedGraph: constructor
     * 
     * @param edges array of arrays containing 3 ints. The first two refer to nodes and the last int refers to the weight of the edge
     * @param numNodes number of nodes in the graph
     */
    public Graph(int[][] edges, int numNodes, boolean isDirected, boolean isWeighted) {
        this.numNodes = numNodes;
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
        edgeWeights = new int[numNodes][numNodes];
        edgeList = new ArrayList<Edge>();
        minSpanningTree = new ArrayList<Edge>();
        nodeList = new ArrayList<Integer>();
        

        for (int i = 0; i < edges.length; i++) {
            int source, dest, weight;
            source = edges[i][0];
            dest = edges[i][1];

            if (this.isWeighted) {
                weight = edges[i][2];
            } else {
                weight = -1;
            }
            
            /*
             * TODO: is it not the case if this is an undirected graph that we should add 2 edge objects here? One for each direction...
             * Not doing this immediately because I want to make sure that it won't mess anything up (after fixing everything else with recent changes)
             */
            Edge newEdge = new Edge(source, dest, weight);
            edgeList.add(newEdge);
            
            // Initialise our edge weights
            edgeWeights[source][dest] = weight;
            if (!this.isDirected) {
                edgeWeights[dest][source] = weight;
            }
        }
        
        for (int i = 0; i < numNodes; i++) {
            nodeList.add(i);
        }

        // Create adjacency list from edges 
        updateAdjList();
    }

    // create/update adjacency list
    public void updateAdjList() {
        adjList = new ArrayList<List<Integer>>();
        // Initialise each list in the adjacency list
        for (int i = 0; i < numNodes; i++) {
            adjList.add(new ArrayList<Integer>());
        }
        for (Edge edge: edgeList) {
            adjList.get(edge.getSource()).add(edge.getDest());
            if (!this.isDirected) {
                adjList.get(edge.getDest()).add(edge.getSource());
            }
        }
    }

    
    public int getEdgeWeight(int source, int dest) {
        return edgeWeights[source][dest];
    }

    // Is it right that we have a function like this here, where the calculation of this depends on a whole other thing executing. NO. Need to clean this up.
    public List<Edge> getMinSpanningTree() {
        return minSpanningTree;
    }

    public void printMinSpanningTree() {
        for(Edge edge: minSpanningTree) {
            Pr.x("Weight: " + edge.getWeight() + ", source: " + edge.getSource() + ", dest: " + edge.getDest());
        }
    }

    public void updateMinSpanningTree(Edge edge) {
        minSpanningTree.add(edge);
    }
    
    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public List<Edge> getOrderedByWeightList() {
        orderedByWeight = orderByWeight(new ArrayList<Edge>(edgeList)); // we copy the list because we destroy it when ordering.
        return orderedByWeight;
    }

    public List<List<Integer>> getAdjList() {
        return adjList;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public List<Integer> getNodeList() {
        return nodeList;
    }



    /**
     * orderByWeight - uses the basic Quicksort Algorithm (recursive)
     * 
     * @param edgeList containing WeightedEdge objects, most likely unsorted (according to edge weight)
     * @return sorted List<WeightedEdge>
     */
    private List<Edge> orderByWeight(List<Edge> edgeList) {
        if (edgeList.size() <= 1) {
            return edgeList;
        } else {
            // Select pivot, arrange numbers either side of pivot and recurse
            Edge pivotEdge = edgeList.get(edgeList.size() - 1);
            int pivot = pivotEdge.getWeight();
            List<Edge> beforePivot = new ArrayList<Edge>();
            List<Edge> afterPivot = new ArrayList<Edge>();

            // Loop through unsorted elements and place them in either beforePivot or afterPivot based on their value relative to pivot
            for (int i = 0; i < edgeList.size() - 1; i++) {
                if (edgeList.get(i).getWeight() <= pivot) {
                    beforePivot.add(edgeList.get(i));
                } else {
                    afterPivot.add(edgeList.get(i));
                }
            }

            List<Edge> beforePivotSorted = orderByWeight(beforePivot); // recurse on elements before pivot
            beforePivotSorted.add(pivotEdge); // add pivot back
            List<Edge> afterPivotSorted = orderByWeight(afterPivot); // recurse on elements after pivot

            beforePivotSorted.addAll(afterPivotSorted); // join the two lists together
            return beforePivotSorted;
        }
    }

    // debug function for checking if sorting of edgeList works... It does.
    public void printEdgeList(List<Edge> edgeList) {
        Pr.x("PRINTING EDGE LIST:");
        Pr.x("=========================================");
        Pr.x("=========================================");
        for (Edge edge: edgeList) {
            edge.print();
        }
        Pr.x("=========================================");
        Pr.x("=========================================");
    }

    // debug function for checking adjList is correctly populated
    public void printAdjList() {
        for (int i = 0; i < adjList.size(); i++) {
            List<Integer> adj = adjList.get(i);
            System.out.println("NODE: " + i);
            System.out.println("Neighbours:");
            for (Integer adjNode: adj) {
                System.out.println(adjNode);
            }
        }
    }
}
