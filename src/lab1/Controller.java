package lab1;

import graphs.DirectedGraph;

import java.util.StringJoiner;

public class Controller
{

    private DirectedGraph directedGraph;

    public Controller(DirectedGraph directedGraph)
    {
        this.directedGraph = directedGraph;
    }

    public void removeVertex(int vertex)
    {
        directedGraph.removeVertex(vertex);
    }

    public void addVertex(int vertex)
    {
        directedGraph.addVertex(vertex);
    }

    public void removeEdge(int vertex1, int vertex2)
    {
        directedGraph.removeEdge(vertex1, vertex2);
    }

    public void addEdge(int vertex1, int vertex2, int cost)
    {
        directedGraph.addEdge(vertex1, vertex2, cost);
    }

    public void setCost(int vertex1, int vertex2, int cost)
    {
        directedGraph.changeCost(vertex1, vertex2, cost);
    }

    public String getCost(int vertex1, int vertex2)
    {
        return String.valueOf(directedGraph.getCost(vertex1, vertex2));
    }

    public String getOutEdges(int vertex)
    {
        Iterable<Integer> edgeSet = directedGraph.parseOutboundEdges(vertex);
        StringJoiner stringJoiner = new StringJoiner(", ");
        edgeSet.forEach((vertex2) -> stringJoiner.add(vertex + "-" + vertex2));
        return stringJoiner.toString();
    }

    public String getInEdges(int vertex)
    {
        Iterable<Integer> edgeSet = directedGraph.parseInboundEdges(vertex);
        StringJoiner stringJoiner = new StringJoiner(", ");
        edgeSet.forEach((vertex1) -> stringJoiner.add(vertex1 + "-" + vertex));
        return stringJoiner.toString();
    }

    public String getOutDegree(int vertex)
    {
        return String.valueOf(directedGraph.outDegree(vertex));
    }

    public String getInDegree(int vertex)
    {
        return String.valueOf(directedGraph.inDegree(vertex));
    }

    public String existsEdge(int vertex1, int vertex2)
    {
        return String.valueOf(directedGraph.existsEdge(vertex1, vertex2));
    }

    public String getVertices()
    {
        Iterable<Integer> vertexSet = directedGraph.parseVertices();
        StringJoiner stringJoiner = new StringJoiner(", ");
        vertexSet.forEach((vertex) -> stringJoiner.add(vertex.toString()));
        return stringJoiner.toString();
    }

    public String getNumberOfVertices()
    {
        return String.valueOf(directedGraph.getNumberOfVertices());
    }
}
