package graphs.algorithms;

import graphs.DirectedGraph;
import graphs.UndirectedGraph;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexDoesNotExistException;

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

    /**
     * Finds the lowest cost walk from the startingVertex to the targetVertex in the directedGraph
     * @return The lowest cost walk from the startingVertex to the targetVertex,
     *          an empty walk if there is no walk between the two
     *          or null if there are negative cost cycles in the graph
     * @throws VertexDoesNotExistException if one of the given vertices is not in the graph
     */
    public static DTOCostWalk lowestCostWalk(DirectedGraph directedGraph, int startVertex, int targetVertex)
    {
        if (!directedGraph.existsVertex(startVertex) || !directedGraph.existsVertex(targetVertex))
        {
            throw new VertexDoesNotExistException();
        }

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

        Status targetMinimum = new Status(INFINITY, null);
        int targetLevel = 0;
        for (int vertex : directedGraph.parseVertices())
        {
            List<Status> vertexStatusList = statusMatrix.get(vertex);
            Status minimum = new Status(INFINITY, null);
            int level = 0;
            for (int i = 0; i < vertexNumber; i++)
            {
                Status status = vertexStatusList.get(i);
                if (status.getCost() < minimum.getCost())
                {
                    minimum = status;
                    level = i;
                }
            }
            if (vertexStatusList.get(vertexNumber).cost < minimum.getCost())
            {
                return null;
            }
            if (vertex == targetVertex)
            {
                targetMinimum.cost = minimum.getCost();
                targetMinimum.parent = minimum.getParent();
                targetLevel = level;
            }
        }

        List<Integer> walk = new LinkedList<>();
        DTOCostWalk result = new DTOCostWalk(targetMinimum.getCost(), walk);

        if (targetMinimum.getCost() == INFINITY)
        {
            return result;
        }
        Status currentStatus;
        Integer currentVertex = targetVertex;
        while (currentVertex != null)
        {
            walk.add(0, currentVertex);
            currentStatus = statusMatrix.get(currentVertex).get(targetLevel);
            currentVertex = currentStatus.getParent();
            targetLevel--;
        }
        return result;
    }
}
