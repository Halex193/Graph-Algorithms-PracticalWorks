package undirectedGraph;

import graphs.OrderedVertexPair;
import graphs.UndirectedGraph;
import graphs.algorithms.Traversal;

import java.util.*;
import java.util.stream.Collectors;

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

    public String showConnectedComponents()
    {
        List<UndirectedGraph> connectedComponents = Traversal.getConnectedComponents(undirectedGraph);
        StringJoiner stringJoiner = new StringJoiner("\n");
        for (int component = 0; component < connectedComponents.size(); component++)
        {
            StringJoiner vertices = new StringJoiner(", ");
            for (int vertex : connectedComponents.get(component).parseVertices())
            {
                vertices.add(Integer.toString(vertex));
            }
            stringJoiner.add("Connected component " + component + " vertices: " + vertices.toString());
            StringJoiner edges = new StringJoiner(", ");
            for (OrderedVertexPair orderedVertexPair : connectedComponents.get(component).parseEdges())
            {
                edges.add(orderedVertexPair.getVertex1() + " - " + orderedVertexPair.getVertex2());
            }
            stringJoiner.add("Connected component " + component + " edges: " + edges.toString());
        }
        return stringJoiner.toString();
    }

    public List<Integer> lowHamiltonian()
    {
        Iterable<OrderedVertexPair> edges = undirectedGraph.parseEdges();
        List<OrderedVertexPair> orderedEdges = new ArrayList<>(undirectedGraph.getNumberOfEdges());
        edges.forEach(orderedEdges::add);
        orderedEdges.sort(Comparator.comparingInt((OrderedVertexPair edge) -> undirectedGraph.getCost(edge.getVertex1(), edge.getVertex2())));

        int numberOfVertices = undirectedGraph.getNumberOfVertices();
        List<OrderedVertexPair> foundEdges = new ArrayList<>(numberOfVertices);
        Iterator<OrderedVertexPair> iterator = orderedEdges.iterator();
        Map<Integer, Integer> parents = new HashMap<>(numberOfVertices);
        Map<Integer, Integer> componentSize = new HashMap<>(numberOfVertices);
        for (Integer vertex : undirectedGraph.parseVertices())
        {
            parents.put(vertex, null);
            componentSize.put(vertex, 1);
        }
        while (foundEdges.size() != numberOfVertices && iterator.hasNext())
        {
            OrderedVertexPair edge = iterator.next();
            if (linkHamiltonian(edge, parents, componentSize))
            {
                foundEdges.add(edge);
            }
        }
        if (foundEdges.size() == numberOfVertices)
        {
            List<Integer> cycle = new ArrayList<>(numberOfVertices);
            Integer lastVertex = null;
            int currentVertex = foundEdges.get(0).getVertex1();
            cycle.add(currentVertex);
            for (int i = 0; i < numberOfVertices; i++)
            {
                final int finalCurrentVertex = currentVertex;
                List<Integer> adjacentEdges = foundEdges.stream()
                        .filter((edge) -> edge.getVertex1() == finalCurrentVertex || edge.getVertex2() == finalCurrentVertex)
                        .map((edge) ->
                        {
                            if (edge.getVertex1() == finalCurrentVertex)
                            {
                                return edge.getVertex2();
                            }
                            return edge.getVertex1();
                        })
                        .collect(Collectors.toList());
                Integer firstNeighbour = adjacentEdges.get(0);
                Integer secondNeighbour = adjacentEdges.get(1);
                if (lastVertex == null)
                {
                    cycle.add(firstNeighbour);
                    lastVertex = currentVertex;
                    currentVertex = firstNeighbour;
                }
                else
                {
                    if (!firstNeighbour.equals(lastVertex))
                    {
                        cycle.add(firstNeighbour);
                        lastVertex = currentVertex;
                        currentVertex = firstNeighbour;
                    }
                    else
                    {
                        cycle.add(secondNeighbour);
                        lastVertex = currentVertex;
                        currentVertex = secondNeighbour;
                    }
                }
            }
        }
        return null;
    }

    private boolean linkHamiltonian(OrderedVertexPair
                                            edge, Map<Integer, Integer> parents, Map<Integer, Integer> componentSize)
    {
        //TODO Last edge should be adjacent to a boundary (5 -> 0, not 5 -> 1)
        int parent1 = edge.getVertex1();
        int parent2 = edge.getVertex2();
        while (parents.get(parent1) != null)
        {
            parent1 = parents.get(parent1);
        }
        while (parents.get(parent2) != null)
        {
            parent2 = parents.get(parent2);
        }
        if (parent1 == parent2 && componentSize.get(parent1) != undirectedGraph.getNumberOfVertices())
        {
            return false;
        }
        parents.put(parent1, parent2);
        componentSize.put(parent2, componentSize.get(parent1) + componentSize.get(parent2));
        return true;
    }

}
