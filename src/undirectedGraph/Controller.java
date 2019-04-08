package undirectedGraph;

import graphs.OrderedVertexPair;
import graphs.UndirectedGraph;

import java.util.*;

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
        Iterable<OrderedVertexPair> edgeSet = undirectedGraph.parseEdges();
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (OrderedVertexPair edge : edgeSet)
        {
            stringJoiner.add(String.format("%d - %d", edge.getVertex1(), edge.getVertex2()));
        }
        return stringJoiner.toString();
    }

    public String getConnectedComponents()
    {
        Map<Integer, Integer> components = new HashMap<>(undirectedGraph.getNumberOfVertices());
        undirectedGraph.parseVertices().forEach((vertex) -> components.put(vertex, 0));

        int componentIndex = 1;
        for (Map.Entry<Integer, Integer> entry : components.entrySet())
        {
            if (entry.getValue() == 0)
            {
                depthFirstSearch(entry.getKey(), components, componentIndex);
                componentIndex++;
            }
        }

        Map<Integer, StringJoiner> connectedComponents = new HashMap<>(componentIndex - 1);
        for (int i = 0; i < componentIndex; i++)
        {
            connectedComponents.put(i, new StringJoiner(" - "));
        }

        for (Map.Entry<Integer, Integer> entry : components.entrySet())
        {
            connectedComponents.get(entry.getValue()).add(Integer.toString(entry.getKey()));
        }

        StringJoiner stringJoiner = new StringJoiner("\n");
        for (StringJoiner component : connectedComponents.values())
        {
            stringJoiner.add(component.toString());
        }
        return stringJoiner.toString();
    }

    private void depthFirstSearch(int initialVertex, Map<Integer, Integer> components, int componentIndex)
    {
        Stack<Integer> stack = new Stack<>();
        stack.push(initialVertex);
        components.put(initialVertex, componentIndex);
        while (!stack.isEmpty())
        {
            int vertex = stack.pop();
            for (int neighbour : undirectedGraph.parseAdjacentEdges(vertex))
            {
                if (components.get(neighbour) == 0)
                {
                    stack.push(neighbour);
                    components.put(neighbour, componentIndex);
                }
            }
        }
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
