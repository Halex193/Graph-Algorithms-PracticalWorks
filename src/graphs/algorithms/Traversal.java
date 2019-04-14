package graphs.algorithms;

import graphs.UndirectedGraph;
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
}
