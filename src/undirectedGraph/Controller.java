package undirectedGraph;

import graphs.OrderedVertexPair;
import graphs.UndirectedGraph;
import graphs.algorithms.Traversal;

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
            for (OrderedVertexPair orderedVertexPair : connectedComponents.get(component)
                                                                          .parseEdges())
            {
                edges.add(orderedVertexPair.getVertex1() + " - " + orderedVertexPair.getVertex2());
            }
            stringJoiner.add("Connected component " + component + " edges: " + edges.toString());
        }
        return stringJoiner.toString();
    }

    public DTOHamiltonian lowHamiltonian()
    {
        List<Integer> cycle = Traversal.lowHamiltonian(undirectedGraph);
        if (cycle == null)
        {
            return null;
        }
        int cost = 0;
        for (int i = 0; i < cycle.size() - 1; i++)
        {
            cost += undirectedGraph.getCost(cycle.get(i), cycle.get(i + 1));
        }
        return new DTOHamiltonian(cycle, cost);
    }

    public class DTOHamiltonian
    {

        private final List<Integer> cycle;
        private final int cost;

        public DTOHamiltonian(List<Integer> cycle, int cost)
        {
            this.cycle = cycle;
            this.cost = cost;
        }

        public List<Integer> getCycle()
        {
            return cycle;
        }

        public int getCost()
        {
            return cost;
        }
    }
}
