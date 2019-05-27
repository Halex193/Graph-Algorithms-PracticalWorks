package graphs.algorithms;

import graphs.DirectedGraph;
import graphs.OrderedVertexPair;
import graphs.UndirectedGraph;
import graphs.exceptions.EdgeAlreadyExistsException;
import graphs.exceptions.VertexDoesNotExistException;

import java.util.*;
import java.util.stream.Collectors;

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
                depthFirstSearchAccessible(
                        undirectedGraph,
                        entry.getKey(),
                        components,
                        componentIndex
                );
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
                    connectedComponents.get(entry.getValue())
                                       .addEdge(entry.getKey(),
                                                vertex2,
                                                undirectedGraph.getCost(entry.getKey(), vertex2)
                                       );
                }
                catch (EdgeAlreadyExistsException ignored)
                {

                }
            }
        }
        return new ArrayList<>(connectedComponents.values());
    }

    /**
     * Marks all the vertices that are connected to the initialVertex with the componentIndex in the components map
     */
    private static void depthFirstSearchAccessible(
            UndirectedGraph undirectedGraph,
            int initialVertex,
            Map<Integer, Integer> components,
            int componentIndex
    )
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

    public static List<Integer> lowHamiltonian(UndirectedGraph undirectedGraph)
    {
        Iterable<OrderedVertexPair> edges = undirectedGraph.parseEdges();
        List<OrderedVertexPair> orderedEdges = new ArrayList<>(undirectedGraph.getNumberOfEdges());
        edges.forEach(orderedEdges::add);
        orderedEdges.sort(Comparator.comparingInt((OrderedVertexPair edge) -> undirectedGraph.getCost(
                edge.getVertex1(),
                edge.getVertex2()
        )));

        int numberOfVertices = undirectedGraph.getNumberOfVertices();
        List<OrderedVertexPair> edgeList = new LinkedList<>();
        Set<OrderedVertexPair> foundEdges = new HashSet<>(numberOfVertices);
        Iterator<OrderedVertexPair> iterator = orderedEdges.iterator();
        Map<Integer, Integer> parents = new HashMap<>(numberOfVertices);
        Map<Integer, Integer> componentSize = new HashMap<>(numberOfVertices);
        for (Integer vertex : undirectedGraph.parseVertices())
        {
            parents.put(vertex, null);
            componentSize.put(vertex, 1);
        }
        OrderedVertexPair lastEdge = null;
        while (foundEdges.size() != numberOfVertices && iterator.hasNext())
        {
            OrderedVertexPair edge = iterator.next();
            if (foundEdges.size() == numberOfVertices - 1)
            {
                if (lastEdge == null)
                {
                    lastEdge = getLastEdge(edgeList, foundEdges);
                }
                if (edge.equals(lastEdge))
                {
                    foundEdges.add(edge);
                    edgeList.add(edge);
                }
            }
            else if (linkHamiltonian(edge, parents, componentSize))
            {
                foundEdges.add(edge);
            }
        }
        if (foundEdges.size() == numberOfVertices)
        {
            List<Integer> cycle = new ArrayList<>(numberOfVertices);
            int firstVertex = getUncommonVertex(edgeList.get(0), edgeList.get(1));
            cycle.add(firstVertex);
            cycle.add(commonVertex(edgeList.get(1), edgeList.get(0)));
            for (int i = 1; i < edgeList.size(); i++)
            {
                cycle.add(getUncommonVertex(edgeList.get(i), edgeList.get(i - 1)));
            }

            return cycle;
        }
        return null;
    }

    private static OrderedVertexPair getLastEdge(
            List<OrderedVertexPair> edgeList,
            Set<OrderedVertexPair> foundEdges
    )
    {
        Set<OrderedVertexPair> edgeSet = new HashSet<>(foundEdges);
        OrderedVertexPair first = edgeSet.iterator().next();
        edgeList.add(first);
        edgeSet.remove(first);
        List<OrderedVertexPair> neighbourEdges = edgeSet.stream()
                                                        .filter((foundEdge) -> commonVertex(
                                                                foundEdge,
                                                                edgeList.get(edgeList.size() - 1)
                                                        ) != null)
                                                        .collect(Collectors.toList());
        while (!neighbourEdges.isEmpty())
        {
            edgeList.add(neighbourEdges.get(0));
            edgeSet.remove(neighbourEdges.get(0));

            neighbourEdges = edgeSet.stream()
                                    .filter((foundEdge) -> commonVertex(
                                            foundEdge,
                                            edgeList.get(edgeList.size() - 1)
                                    ) != null)
                                    .collect(Collectors.toList());

        }

        neighbourEdges = edgeSet.stream()
                                .filter((foundEdge) -> commonVertex(
                                        foundEdge,
                                        edgeList.get(0)
                                ) != null)
                                .collect(Collectors.toList());

        while (!neighbourEdges.isEmpty())
        {
            edgeList.add(0, neighbourEdges.get(0));
            edgeSet.remove(neighbourEdges.get(0));

            neighbourEdges = edgeSet.stream()
                                    .filter((foundEdge) -> commonVertex(
                                            foundEdge,
                                            edgeList.get(0)
                                    ) != null)
                                    .collect(Collectors.toList());

        }

        int p1 = edgeList.size() - 1;
        int p2 = 0;
        int vertex1 = getUncommonVertex(
                edgeList.get(p1),
                edgeList.get(p1 - 1)
        );
        int vertex2 = getUncommonVertex(
                edgeList.get(p2),
                edgeList.get(p2 + 1)
        );
        return new OrderedVertexPair(vertex1, vertex2);
    }

    private static int getUncommonVertex(OrderedVertexPair edge1, OrderedVertexPair edge2)
    {
        if (!edge1.getVertex1().equals(commonVertex(edge1, edge2)))
        {
            return edge1.getVertex1();
        }
        return edge1.getVertex2();
    }

    private static Integer commonVertex(OrderedVertexPair edge1, OrderedVertexPair edge2)
    {
        if (edge1.getVertex1().equals(edge2.getVertex1()) ||
                edge1.getVertex1().equals(edge2.getVertex2()))
        {
            return edge1.getVertex1();
        }
        if (edge1.getVertex2().equals(edge2.getVertex1()) ||
                edge1.getVertex2().equals(edge2.getVertex2()))
        {
            return edge1.getVertex2();
        }
        return null;
    }

    private static boolean linkHamiltonian(
            OrderedVertexPair
                    edge, Map<Integer, Integer> parents, Map<Integer, Integer> componentSize
    )
    {
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
        if (parent1 == parent2)
        {
            return false;
        }
        parents.put(parent1, parent2);
        componentSize.put(parent2, componentSize.get(parent1) + componentSize.get(parent2));
        return true;
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
     *
     * @return The lowest cost walk from the startingVertex to the targetVertex,
     * an empty walk if there is no walk between the two
     * or null if there are negative cost cycles in the graph
     * @throws VertexDoesNotExistException if one of the given vertices is not in the graph
     */
    public static DTOCostWalk lowestCostWalk(
            DirectedGraph directedGraph,
            int startVertex,
            int targetVertex
    )
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
