/*

2008mar18

*/

package m00;

public class Edge {
    private Vertex from, to;

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }
    
    public Vertex getFrom() {
        return this.from;
    }
    
    public Vertex getTo() {
        return this.to;
    }
}
