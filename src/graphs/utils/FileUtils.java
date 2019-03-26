package graphs.utils;

import graphs.DirectedGraph;
import graphs.UndirectedGraph;
import graphs.VertexPair;

import java.io.*;
import java.nio.file.Files;
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
        scanner.close();
        return directedGraph;
    }

    public static void writeDirectedGraphToFile(DirectedGraph directedGraph, String fileName) throws IOException
    {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter writer = new PrintWriter(fileWriter);
        writer.printf("%d %d\n", directedGraph.getNumberOfVertices(), directedGraph.getNumberOfEdges());
        for (VertexPair edge : directedGraph.parseEdges())
        {
            int cost = directedGraph.getCost(edge.getVertex1(), edge.getVertex2());
            writer.printf("%d %d %d\n", edge.getVertex1(), edge.getVertex2(), cost);
        }
        writer.close();
    }

    public static UndirectedGraph createUndirectedGraphFromFile(String fileName) throws FileNotFoundException
    {
        return null;
    }
}
