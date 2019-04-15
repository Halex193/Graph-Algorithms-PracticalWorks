package graphs.algorithms;

import graphs.DirectedGraph;
import graphs.UndirectedGraph;
import graphs.VertexPair;
import graphs.exceptions.EdgeAlreadyExistsException;

import java.util.*;

public class Traversal
{
    /**
     * Computes the connected components of the graph
     */
    public static List<UndirectedGraph> getConnectedComponents(UndirectedGraph undirectedGraph)
    {
        Map<Integer, Integer> components = new HashMap<>(undirectedGraph.getNumberOfVertices());
        undirectedGraph.parseVertices().forEach((vertex) -> components.put(vertex, 0));

        int componentIndex = 1;
        for (Map.Entry<Integer, Integer> entry : components.entrySet())
        {
            if (entry.getValue() == 0)
            {
                depthFirstSearchAccessible(undirectedGraph, entry.getKey(), components, componentIndex);
                componentIndex++;
            }
        }

        Map<Integer, UndirectedGraph> connectedComponents = new HashMap<>(componentIndex - 1);
        for (int i = 1; i < componentIndex; i++)
        {
            connectedComponents.put(i, new UndirectedGraph());
        }
        for (Map.Entry<Integer, Integer> entry : components.entrySet())
        {
            connectedComponents.get(entry.getValue()).addVertex(entry.getKey());
        }
        for (Map.Entry<Integer, Integer> entry : components.entrySet())
        {
            for (int vertex2 : undirectedGraph.parseAdjacentEdges(entry.getKey()))
            {
                try
                {
                    connectedComponents.get(entry.getValue()).addEdge(entry.getKey(), vertex2, undirectedGraph.getCost(entry.getKey(), vertex2));
                } catch (EdgeAlreadyExistsException ignored)
                {

                }
            }
        }
        return new ArrayList<>(connectedComponents.values());
    }

    /**
     * Marks all the vertices that are connected to the initialVertex with the componentIndex in the components map
     */
    private static void depthFirstSearchAccessible(UndirectedGraph undirectedGraph, int initialVertex, Map<Integer, Integer> components, int componentIndex)
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

    private static class Status
    {
        private int cost;
        private Integer parent;

        public Status(int cost, Integer parent)
        {
            this.cost = cost;
            this.parent = parent;
        }

        public int getCost()
        {
            return cost;
        }

        public void setCost(int cost)
        {
            this.cost = cost;
        }

        public Integer getParent()
        {
            return parent;
        }

        public void setParent(Integer parent)
        {
            this.parent = parent;
        }
    }

    public static class DTOCostWalk
    {
        private int cost;
        private List<Integer> walk;

        public DTOCostWalk(int cost, List<Integer> walk)
        {
            this.cost = cost;
            this.walk = walk;
        }

        public int getCost()
        {
            return cost;
        }

        public List<Integer> getWalk()
        {
            return walk;
        }
    }

    public static DTOCostWalk lowestCostWalk(DirectedGraph directedGraph, int startVertex, int targetVertex)
    {
        final int INFINITY = Integer.MAX_VALUE;
        int vertexNumber = directedGraph.getNumberOfVertices();
        Map<Integer, List<Status>> statusMatrix = new HashMap<>(vertexNumber);

        for (int vertex : directedGraph.parseVertices())
        {
            statusMatrix.put(vertex, new ArrayList<>(vertexNumber + 1));
        }

        for (Map.Entry<Integer, List<Status>> entry : statusMatrix.entrySet())
        {
            entry.getValue().add(new Status(INFINITY, null));
        }
        statusMatrix.get(startVertex).get(0).setCost(0);

        for (int i = 1; i <= vertexNumber; i++)
        {
            for (int vertex : directedGraph.parseVertices())
            {
                Status minimum = new Status(INFINITY, null);

                for (int neighbour : directedGraph.parseInboundEdges(vertex))
                {
                    int previousCost = statusMatrix.get(neighbour).get(i - 1).getCost();
                    if (previousCost != INFINITY)
                    {
                        int cost = previousCost + directedGraph.getCost(neighbour, vertex);
                        if (cost < minimum.getCost())
                        {
                            minimum.setCost(cost);
                            minimum.setParent(neighbour);
                        }
                    }
                }
                statusMatrix.get(vertex).add(minimum);
            }
        }
        for (int vertex : directedGraph.parseVertices())
        {
            List<Status> costsForVertex = statusMatrix.get(vertex);
            if (costsForVertex.get(vertexNumber).getCost() < (costsForVertex.get(vertexNumber - 1).getCost()))
            {
                return null;
            }
        }

        List<Status> targetVertexStatusList = statusMatrix.get(targetVertex);
        Status minimum = new Status(INFINITY, null);
        int level = 0;
        for (int i = 0; i < vertexNumber; i++)
        {
            Status status = targetVertexStatusList.get(i);
            if (status.getCost() < minimum.getCost())
            {
                minimum = status;
                level = i;
            }
        }
        List<Integer> walk = new LinkedList<>();
        DTOCostWalk result = new DTOCostWalk(minimum.getCost(), walk);

        if (minimum.getCost() == INFINITY)
        {
            return result;
        }
        Status currentStatus;
        Integer currentVertex = targetVertex;
        while (currentVertex!= null)
        {
            walk.add(0, currentVertex);
            currentStatus = statusMatrix.get(currentVertex).get(level);
            currentVertex = currentStatus.getParent();
            level--;
        }
        return result;
    }
}
