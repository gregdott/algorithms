/*
 * Author: Gregory Dott
 * 27-10-2022
 * 
 * A simple class to represent an edge in a graph. Can be used for weighted and unweighted edges.
 * In the case of the edge being unweighted, the value of weight gets set to -1 here.
 * This can be used in both directed and undirected graphs. In the case of undirected graphs, we
 * just end up with double the number of edge objects (1 edge object for each edge direction) to 
 * correctly represent the graph. 
 */

public class Edge {
    private int source, dest, weight;

    /**
     * Edge - class constructor
     * @param source the source vertex
     * @param dest the destination vertex
     * @param weight the weight of the edge (-1 if unweighted)
     */
    public Edge(int source, int dest, int weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public int getSource() {
        return source;
    }

    public int getDest() {
        return dest;
    }

    public void print() {
        Pr.x("Weight: " + weight + ", source: " + getSource() + ", dest: " + getDest());
    }
}
