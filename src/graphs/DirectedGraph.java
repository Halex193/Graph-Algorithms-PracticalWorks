package graphs;

import java.util.*;

/**
 * Models a directed Graph data structure
 */
public class DirectedGraph implements Graph
{
    protected Map<Integer, List<Integer>> inEdges;
    protected Map<Integer, List<Integer>> outEdges;
    protected Map<Pair, Integer> edges;

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

    public Iterator<Integer> parseInboundEdges(int vertex)
    {
        return inEdges.get(vertex).iterator();
    }

    public Iterator<Integer> parseOutboundEdges(int vertex)
    {
        return outEdges.get(vertex).iterator();
    }

    @Override
    public int getNumberOfEdges()
    {
        return edges.size();
    }

    @Override
    public Iterator<Integer> parseVertices()
    {
        return inEdges.keySet().iterator();
    }

    @Override
    public boolean existsEdge(int vertex1, int vertex2)
    {
        return edges.containsKey(new Pair(vertex1, vertex2));
    }

    @Override
    public int getCost(int vertex1, int vertex2)
    {
        if (!existsEdge(vertex1, vertex2))
        {
            throw new EdgeDoesNotExistException();
        }
        return edges.get(new Pair(vertex1, vertex2));
    }

    @Override
    public void changeCost(int vertex1, int vertex2, int newCost)
    {
        if (!existsEdge(vertex1, vertex2))
        {
            throw new EdgeDoesNotExistException();
        }
        edges.put(new Pair(vertex1, vertex2), newCost);
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
        edges.put(new Pair(vertex1, vertex2), cost);
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

        inEdges.get(vertex2).remove(vertex1);
        outEdges.get(vertex1).remove(vertex2);
        edges.remove(new Pair(vertex1, vertex2));
    }

    @Override
    public DirectedGraph copy()
    {
        DirectedGraph newGraph = new DirectedGraph();
        for (Integer vertex : inEdges.keySet())
        {
            newGraph.addVertex(vertex);
        }
        for (Pair edge : edges.keySet())
        {
            newGraph.addEdge(edge.getElement1(), edge.getElement2(), edges.get(edge));
        }
        return newGraph;
    }

    public static class Pair
    {
        private final Integer element1;
        private final Integer element2;

        public Pair(Integer element1, Integer element2)
        {
            this.element1 = element1;
            this.element2 = element2;
        }

        public Integer getElement1()
        {
            return element1;
        }

        public Integer getElement2()
        {
            return element2;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return Objects.equals(element1, pair.element1) &&
                    Objects.equals(element2, pair.element2);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(element1, element2);
        }
    }
    public static class VertexAlreadyExistsException extends RuntimeException
    {

    }
    public static class VertexDoesNotExistException extends RuntimeException
    {

    }
    public static class EdgeDoesNotExistException extends RuntimeException
    {
    }

    public static class EdgeAlreadyExistsException extends RuntimeException
    {
    }
}
