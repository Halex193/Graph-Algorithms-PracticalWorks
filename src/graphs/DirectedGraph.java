package graphs;

import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.EdgeDoesNotExistException;
import graphs.exceptions.VertexAlreadyExistsException;
import graphs.exceptions.VertexDoesNotExistException;

import java.util.*;

/**
 * Models a directed graph data structure
 */
public class DirectedGraph implements Graph
{
    protected Map<Integer, List<Integer>> inEdges;
    protected Map<Integer, List<Integer>> outEdges;
    protected Map<VertexPair, Integer> edges;

    public DirectedGraph()
    {
        inEdges = new HashMap<>();
        outEdges = new HashMap<>();
        edges = new HashMap<>();
    }

    public DirectedGraph(int initialVertexNumber, int initialEdgeNumber)
    {
        inEdges = new HashMap<>(initialVertexNumber);
        outEdges = new HashMap<>(initialVertexNumber);
        edges = new HashMap<>(initialEdgeNumber);

        for (int i = 0; i < initialVertexNumber; i++)
        {
            addVertex(i);
        }
    }

    @Override
    public int getNumberOfVertices()
    {
        return inEdges.size();
    }

    @Override
    public int getNumberOfEdges()
    {
        return edges.size();
    }

    public int inDegree(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }

        return inEdges.get(vertex).size();
    }

    public int outDegree(int vertex)
    {
        if (!outEdges.containsKey(vertex))
        {
            throw new VertexDoesNotExistException();
        }

        return outEdges.get(vertex).size();
    }

    public Iterable<Integer> parseInboundEdges(int vertex)
    {
        return Collections.unmodifiableList(inEdges.get(vertex));
    }

    public Iterable<Integer> parseOutboundEdges(int vertex)
    {
        return Collections.unmodifiableList(outEdges.get(vertex));
    }

    @Override
    public Iterable<Integer> parseVertices()
    {
        return Collections.unmodifiableSet(inEdges.keySet());
    }

    @Override
    public boolean existsEdge(int vertex1, int vertex2)
    {
        return edges.containsKey(new VertexPair(vertex1, vertex2));
    }

    @Override
    public int getCost(int vertex1, int vertex2)
    {
        if (!existsEdge(vertex1, vertex2))
        {
            throw new EdgeDoesNotExistException();
        }
        return edges.get(new VertexPair(vertex1, vertex2));
    }

    @Override
    public void changeCost(int vertex1, int vertex2, int newCost)
    {
        if (!existsEdge(vertex1, vertex2))
        {
            throw new EdgeDoesNotExistException();
        }
        edges.put(new VertexPair(vertex1, vertex2), newCost);
    }

    @Override
    public void addVertex(int vertex)
    {
        if (existsVertex(vertex))
        {
            throw new VertexAlreadyExistsException();
        }
        inEdges.put(vertex, new ArrayList<>());
        outEdges.put(vertex, new ArrayList<>());
    }

    public boolean existsVertex(int vertex)
    {
        return inEdges.containsKey(vertex);
    }

    @Override
    public void removeVertex(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }

        List<Integer> inEdgesList = inEdges.get(vertex);
        while (!inEdgesList.isEmpty())
        {
            int vertex1 = inEdgesList.get(0);
            removeEdge(vertex1, vertex);
        }
        List<Integer> outEdgesList = outEdges.get(vertex);
        while (!outEdgesList.isEmpty())
        {
            int vertex2 = outEdgesList.get(0);
            removeEdge(vertex, vertex2);
        }
        inEdges.remove(vertex);
        outEdges.remove(vertex);
    }

    @Override
    public void addEdge(int vertex1, int vertex2, int cost)
    {
        if (!existsVertex(vertex1))
        {
            throw new VertexDoesNotExistException();
        }
        if (!existsVertex(vertex2))
        {
            throw new VertexDoesNotExistException();
        }
        if (existsEdge(vertex1, vertex2))
        {
            throw new EdgeAlreadyExistsException();
        }
        inEdges.get(vertex2).add(vertex1);
        outEdges.get(vertex1).add(vertex2);
        edges.put(new VertexPair(vertex1, vertex2), cost);
    }

    @Override
    public void removeEdge(int vertex1, int vertex2)
    {
        if (!existsVertex(vertex1))
        {
            throw new VertexDoesNotExistException();
        }
        if (!existsVertex(vertex2))
        {
            throw new VertexDoesNotExistException();
        }
        if (!existsEdge(vertex1, vertex2))
        {
            throw new EdgeDoesNotExistException();
        }

        inEdges.get(vertex2).remove(Integer.valueOf(vertex1));
        outEdges.get(vertex1).remove(Integer.valueOf(vertex2));
        edges.remove(new VertexPair(vertex1, vertex2));
    }

    @Override
    public Iterable<VertexPair> parseEdges()
    {
        return Collections.unmodifiableSet(edges.keySet());
    }

    @Override
    public DirectedGraph copy()
    {
        DirectedGraph newGraph = new DirectedGraph();
        for (Integer vertex : inEdges.keySet())
        {
            newGraph.addVertex(vertex);
        }
        for (VertexPair edge : edges.keySet())
        {
            newGraph.addEdge(edge.getVertex1(), edge.getVertex2(), edges.get(edge));
        }
        return newGraph;
    }
}
