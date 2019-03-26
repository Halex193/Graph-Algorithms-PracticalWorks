package lab2;

import graphs.DirectedGraph;
import graphs.UndirectedGraph;
import graphs.VertexPair;
import graphs.utils.FileUtils;
import graphs.utils.GraphUtils;

import java.io.IOException;
import java.util.StringJoiner;

class Controller
{

    private UndirectedGraph undirectedGraph;

    public Controller(UndirectedGraph undirectedGraph)
    {
        this.undirectedGraph = undirectedGraph;
    }

    public void removeVertex(int vertex)
    {
        undirectedGraph.removeVertex(vertex);
    }

    public void addVertex(int vertex)
    {
        undirectedGraph.addVertex(vertex);
    }

    public void removeEdge(int vertex1, int vertex2)
    {
        undirectedGraph.removeEdge(vertex1, vertex2);
    }

    public void addEdge(int vertex1, int vertex2, int cost)
    {
        undirectedGraph.addEdge(vertex1, vertex2, cost);
    }

    public void setCost(int vertex1, int vertex2, int cost)
    {
        undirectedGraph.changeCost(vertex1, vertex2, cost);
    }

    public String getCost(int vertex1, int vertex2)
    {
        return String.valueOf(undirectedGraph.getCost(vertex1, vertex2));
    }

    public String getAdjacentEdges(int vertex)
    {
        Iterable<Integer> edgeSet = undirectedGraph.parseAdjacentEdges(vertex);
        StringJoiner stringJoiner = new StringJoiner(", ");
        edgeSet.forEach((vertex2) -> stringJoiner.add(vertex + "-" + vertex2));
        return stringJoiner.toString();
    }

    public String getDegree(int vertex)
    {
        return String.valueOf(undirectedGraph.degree(vertex));
    }

    public boolean existsEdge(int vertex1, int vertex2)
    {
        return undirectedGraph.existsEdge(vertex1, vertex2);
    }

    public String getVertices()
    {
        Iterable<Integer> vertexSet = undirectedGraph.parseVertices();
        StringJoiner stringJoiner = new StringJoiner(", ");
        vertexSet.forEach((vertex) -> stringJoiner.add(vertex.toString()));
        return stringJoiner.toString();
    }

    public String getNumberOfVertices()
    {
        return String.valueOf(undirectedGraph.getNumberOfVertices());
    }

    public String getEdges()
    {
        Iterable<VertexPair> edgeSet = undirectedGraph.parseEdges();
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (VertexPair edge : edgeSet)
        {
            stringJoiner.add(String.format("%d - %d", edge.getVertex1(), edge.getVertex2()));
        }
        return stringJoiner.toString();
    }

//    public String writeGraphToFile(String fileName)
//    {
//        try
//        {
//            FileUtils.writeUndirctedGraphToFile(undirectedGraph, fileName);
//            return "The graph was written to the file\n";
//        } catch (IOException e)
//        {
//            return "File cannot be written to\n";
//        }
//    }
}
