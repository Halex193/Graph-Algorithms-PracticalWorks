package directedGraph;

import graphs.DirectedGraph;
import graphs.VertexPair;
import graphs.algorithms.Traversal;
import graphs.utils.FileUtils;
import graphs.utils.GraphUtils;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

class Controller
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

    public boolean existsEdge(int vertex1, int vertex2)
    {
        return directedGraph.existsEdge(vertex1, vertex2);
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

    public void generateRandomGraph(int vertexNumber, int edgeNumber)
    {
        this.directedGraph = GraphUtils.createRandomDirectedGraph(vertexNumber, edgeNumber);
    }

    public String getEdges()
    {
        Iterable<VertexPair> edgeSet = directedGraph.parseEdges();
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (VertexPair edge : edgeSet)
        {
            stringJoiner.add(String.format("%d - %d", edge.getVertex1(), edge.getVertex2()));
        }
        return stringJoiner.toString();
    }

    public String isolatedVertices()
    {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (int vertex : directedGraph.parseVertices())
        {
            if (directedGraph.inDegree(vertex) == 0 && directedGraph.outDegree(vertex) == 0)
            {
                stringJoiner.add(String.valueOf(vertex));
            }
        }
        return stringJoiner.toString();
    }

    public String writeGraphToFile(String fileName)
    {
        try
        {
            FileUtils.writeDirectedGraphToFile(directedGraph, fileName);
            return "The graph was written to the file\n";
        } catch (IOException e)
        {
            return "File cannot be written to\n";
        }
    }

    public String lowestCostWalk(int start, int target)
    {
        Traversal.DTOCostWalk result = Traversal.lowestCostWalk(directedGraph, start, target);
        if (result == null)
        {
            return "The graph contains negative cycles";
        }
        List<Integer> walk = result.getWalk();
        if (walk.isEmpty())
        {
            return "There is no walk from the starting vertex to the target vertex";
        }

        StringJoiner stringJoiner = new StringJoiner(" - ");
        for (Integer edge : walk)
        {
            stringJoiner.add(edge.toString());
        }
        return String.format("The minimum cost is: %d\nThe walk is: %s", result.getCost(), stringJoiner.toString());
    }
}
