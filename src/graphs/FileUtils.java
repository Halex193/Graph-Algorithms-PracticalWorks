package graphs;

import java.util.Scanner;

public class FileUtils
{
    public static DirectedGraph createDirectedGraphFromFile(String fileName)
    {
        Scanner scanner = new Scanner(fileName);
        int vertexNumber = scanner.nextInt();
        int edgeNumber = scanner.nextInt();
        DirectedGraph directedGraph = new DirectedGraph(vertexNumber, edgeNumber);
        for (int i = 0; i < edgeNumber; i++)
        {
            int vertex1 = scanner.nextInt();
            int vertex2 = scanner.nextInt();
            int cost = scanner.nextInt();
            directedGraph.addEdge(vertex1, vertex2, cost);
        }
        return directedGraph;
    }
}
