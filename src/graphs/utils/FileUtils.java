package graphs.utils;

import graphs.DirectedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtils
{
    public static DirectedGraph createDirectedGraphFromFile(String fileName) throws FileNotFoundException
    {
        File file = new File(fileName);
        if (!file.exists())
        {
            throw new FileNotFoundException();
        }
        Scanner scanner = new Scanner(file);
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
