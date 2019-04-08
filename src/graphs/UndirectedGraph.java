package graphs;

import graphs.exceptions.*;

import java.util.*;

/**
 * Models an undirected Graph data structure
 */
public class UndirectedGraph implements Graph
{
    /**
     * Maps the vertices to a list of their neighbour vertices
     */
    protected Map<Integer, List<Integer>> neighbours;

    /**
     * Maps the edges to their associated cost
     */
    protected Map<OrderedVertexPair, Integer> edges;

    /**
     * Creates an empty graph
     */
    public UndirectedGraph()
    {
        neighbours = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * Creates a graph with the specified number of vertices
     * @param initialVertexNumber Initial number of vertices of the graph
     * @param initialEdgeNumber The space to allocate for the edges of the graph
     */
    public UndirectedGraph(int initialVertexNumber, int initialEdgeNumber)
    {
        neighbours = new HashMap<>(initialVertexNumber);
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
        return neighbours.size();
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
     * Computes the degree of the specified vertex
     * @param vertex The vertex for which to calculate the degree
     * @return The computed degree
     */
    public int degree(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }

        return neighbours.get(vertex).size();
    }

    /**
     * Returns an iterable with the edges adjacent to the specified vertex
     * @param vertex The vertex to be searched
     * @return The iterable of edges
     */
    public Iterable<Integer> parseAdjacentEdges(int vertex)
    {
        if (!existsVertex(vertex))
        {
            throw new VertexDoesNotExistException();
        }
        return Collections.unmodifiableList(neighbours.get(vertex));
    }

    /**
     * Returns an iterable with the vertices of the graph
     * @return An iterable with the vertices
     */
    @Override
    public Iterable<Integer> parseVertices()
    {
        return Collections.unmodifiableSet(neighbours.keySet());
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
        return edges.containsKey(new OrderedVertexPair(vertex1, vertex2));
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
        return edges.get(new OrderedVertexPair(vertex1, vertex2));
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
        edges.put(new OrderedVertexPair(vertex1, vertex2), newCost);
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
        neighbours.put(vertex, new ArrayList<>());
    }

    /**
     * Checks if the specified vertex exists
     * @param vertex The vertex to be checked
     * @return true if the vertex is in the graph, false otherwise
     */
    public boolean existsVertex(int vertex)
    {
        return neighbours.containsKey(vertex);
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

        List<Integer> inEdgesList = neighbours.get(vertex);
        while (!inEdgesList.isEmpty())
        {
            int vertex1 = inEdgesList.get(0);
            removeEdge(vertex1, vertex);
        }
        neighbours.remove(vertex);
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
        if (vertex1 == vertex2)
        {
            throw new CannotHaveLoopsException();
        }
        if (existsEdge(vertex1, vertex2))
        {
            throw new EdgeAlreadyExistsException();
        }
        neighbours.get(vertex2).add(vertex1);
        neighbours.get(vertex1).add(vertex2);
        edges.put(new OrderedVertexPair(vertex1, vertex2), cost);
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

        neighbours.get(vertex2).remove(Integer.valueOf(vertex1));
        neighbours.get(vertex1).remove(Integer.valueOf(vertex2));
        edges.remove(new OrderedVertexPair(vertex1, vertex2));
    }

    /**
     * Returns an iterable containing the edges of the graph
     * @return An iterable of VertexPair objects
     */
    @Override
    public Iterable<OrderedVertexPair> parseEdges()
    {
        return Collections.unmodifiableSet(edges.keySet());
    }

    /**
     * Returns a copy of the graph
     * @return The copy as a new UndirectedGraph object
     */
    @Override
    public UndirectedGraph copy()
    {
        UndirectedGraph newGraph = new UndirectedGraph();
        for (Integer vertex : neighbours.keySet())
        {
            newGraph.addVertex(vertex);
        }
        for (OrderedVertexPair edge : edges.keySet())
        {
            newGraph.addEdge(edge.getVertex1(), edge.getVertex2(), edges.get(edge));
        }
        return newGraph;
    }
}
