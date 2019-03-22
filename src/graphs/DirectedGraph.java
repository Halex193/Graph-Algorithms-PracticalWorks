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
    /**
     * Maps the vertices to a list of their inbound edges
     */
    protected Map<Integer, List<Integer>> inEdges;
    /**
     * Maps the vertices to a list of their outbound edges
     */
    protected Map<Integer, List<Integer>> outEdges;
    /**
     * Maps the edges to their associated cost
     */
    protected Map<VertexPair, Integer> edges;

    /**
     * Creates an empty graph
     */
    public DirectedGraph()
    {
        inEdges = new HashMap<>();
        outEdges = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * Creates a graph with the specified number of vertices
     * @param initialVertexNumber Initial number of vertices of the graph
     * @param initialEdgeNumber The space to allocate for the edges of the graph
     */
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

    /**
     * Get the number of vertices of the graph
     * @return The number of vertices
     */
    @Override
    public int getNumberOfVertices()
    {
        return inEdges.size();
    }

    /**
     * Gets the number of edges of the graph
     * @return THe number of edges
     */
    @Override
    public int getNumberOfEdges()
    {
        return edges.size();
    }

    /**
     * Computes the in degree of the specified vertex
     * @param vertex The vertex for which to calculate the in degree
     * @return The computed in degree
     */
    public int inDegree(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }

        return inEdges.get(vertex).size();
    }

    /**
     * Computes the out degree of the specified vertex
     * @param vertex The vertex for which to calculate the out degree
     * @return The computed out degree
     */
    public int outDegree(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }

        return outEdges.get(vertex).size();
    }

    /**
     * Returns an iterable with the inbound edges of the specified vertex
     * @param vertex The vertex to be searched
     * @return The iterable of edges
     */
    public Iterable<Integer> parseInboundEdges(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }
        return Collections.unmodifiableList(inEdges.get(vertex));
    }

    /**
     * Returns an iterable with the outbound edges of the specified vertex
     * @param vertex The vertex to be searched
     * @return The iterable of edges
     */
    public Iterable<Integer> parseOutboundEdges(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }
        return Collections.unmodifiableList(outEdges.get(vertex));
    }

    /**
     * Returns an iterable with the vertices of the graph
     * @return An iterable with the vertices
     */
    @Override
    public Iterable<Integer> parseVertices()
    {
        return Collections.unmodifiableSet(inEdges.keySet());
    }

    /**
     * Checks if the specified edge exists in the graph
     * @param vertex1 The vertex from where the edge starts
     * @param vertex2 The vertex where the edge ends
     * @return true if the edge exists, false otherwise
     */
    @Override
    public boolean existsEdge(int vertex1, int vertex2)
    {
        return edges.containsKey(new VertexPair(vertex1, vertex2));
    }

    /**
     * Gets the cost of the specified edge
     * @param vertex1 The vertex from where the edge starts
     * @param vertex2 The vertex where the edge ends
     * @return The cost of the edge
     */
    @Override
    public int getCost(int vertex1, int vertex2)
    {
        if (!existsEdge(vertex1, vertex2))
        {
            throw new EdgeDoesNotExistException();
        }
        return edges.get(new VertexPair(vertex1, vertex2));
    }

    /**
     * Change the cost of the specified edge
     * @param vertex1 The vertex from where the edge starts
     * @param vertex2 The vertex where the edge ends
     * @param newCost The new cost of the edge
     */
    @Override
    public void changeCost(int vertex1, int vertex2, int newCost)
    {
        if (!existsEdge(vertex1, vertex2))
        {
            throw new EdgeDoesNotExistException();
        }
        edges.put(new VertexPair(vertex1, vertex2), newCost);
    }

    /**
     * Adds the specified vertex to the graph
     * @param vertex The vertex to be added to the graph
     */
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

    /**
     * Checks if the specified vertex exists
     * @param vertex The vertex to be checked
     * @return true if the vertex is in the graph, false otherwise
     */
    public boolean existsVertex(int vertex)
    {
        return inEdges.containsKey(vertex);
    }

    /**
     * Remove the specified vertex
     * @param vertex The vertex to be removed from the graph
     */
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

    /**
     * Adds an edge to the graph
     * @param vertex1 The vertex from where the edge starts
     * @param vertex2 The vertex where the edge ends
     * @param cost The cost of the edge
     */
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

    /**
     * Removed an edge from the graph
     * @param vertex1 The vertex from where the edge starts
     * @param vertex2 The vertex where the edge ends
     */
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

    /**
     * Returns an iterable containing the edges of the graph
     * @return An iterable of VertexPair objects
     */
    @Override
    public Iterable<VertexPair> parseEdges()
    {
        return Collections.unmodifiableSet(edges.keySet());
    }

    /**
     * Returns a copy of the graph
     * @return The copy as a new DirectedGraph object
     */
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
