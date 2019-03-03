package graphs.utils;

import graphs.DirectedGraph;
import graphs.exceptions.EdgeAlreadyExistsException;

import java.util.concurrent.ThreadLocalRandom;

public class GraphUtils
{
    public static DirectedGraph createRandomDirectedGraph(int numberOfVertices, int numberOfEdges)
    {
        DirectedGraph newGraph = new DirectedGraph(numberOfVertices, numberOfEdges);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < numberOfEdges; i++)
        {
            boolean found = false;
            while (!found)
            {
                int randomInt1 = random.nextInt(numberOfVertices);
                int randomInt2 = random.nextInt(numberOfVertices);
                int randomInt3 = random.nextInt(Integer.MAX_VALUE);
                try
                {
                    newGraph.addEdge(randomInt1, randomInt2, randomInt3);
                    found = true;
                } catch (EdgeAlreadyExistsException ignored)
                {

                }
            }
        }
        return newGraph;
    }
}
