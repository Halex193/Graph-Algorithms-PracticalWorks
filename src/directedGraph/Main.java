package directedGraph;

import graphs.DirectedGraph;
import graphs.utils.FileUtils;

import java.io.FileNotFoundException;

class Main
{
    private static final String fileName = "data/graph.txt";

    public static void main(String[] args)
    {
        DirectedGraph directedGraph;
        try
        {
            directedGraph = FileUtils.createDirectedGraphFromFile(fileName);
        } catch (FileNotFoundException e)
        {
            System.out.println(String.format("File \"%s\" was not found", fileName));
            directedGraph = new DirectedGraph();
        }
        Controller controller = new Controller(directedGraph);
        ConsoleUI consoleUI = new ConsoleUI(controller);
        consoleUI.run();
    }
}
