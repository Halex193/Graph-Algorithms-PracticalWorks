package lab1;

import java.util.Scanner;

public class ConsoleUI
{
    private Controller controller;
    private Scanner scanner = new Scanner(System.in);

    public ConsoleUI(Controller controller)
    {
        this.controller = controller;
    }

    public void run()
    {
        while (true)
        {
            System.out.println(
                    "\nMenu\n" +
                            "1. Number of vertices\n" +
                            "2. List vertices\n" +
                            "3. Check if edge exists\n" +
                            "4. Get in degree\n" +
                            "5. Get out degree\n" +
                            "6. Get inbound edges\n" +
                            "7. Get outbound edges\n" +
                            "8. Get edge cost\n" +
                            "9. Set edge cost\n" +
                            "10. Add edge\n" +
                            "11. Remove edge\n" +
                            "12. Add vertex\n" +
                            "13. Remove vertex\n" +
                            "0. Exit\n" +
                            "Your choice: "
            );
            int option = scanner.nextInt();
            if (option == 0)
            {
                break;
            }
            try
            {
                executeOption(option);
            } catch (Exception e)
            {
                // TODO: 27-Feb-19 add exception handling
                e.printStackTrace();
            }
        }
    }

    private void executeOption(int option)
    {
        switch (option)
        {
            case 1:
                numberOfVertices();
                break;
            case 2:
                listVertices();
                break;
            case 3:
                edgeExists();
                break;
            case 4:
                getInDegree();
                break;
            case 5:
                getOutDegree();
                break;
            case 6:
                inboundEdges();
                break;
            case 7:
                outboundEdges();
                break;
            case 8:
                getEdgeCost();
                break;
            case 9:
                setEdgeCost();
                break;
            case 10:
                addEdge();
                break;
            case 11:
                removeEdge();
                break;
            case 12:
                addVertex();
                break;
            case 13:
                removeVertex();
                break;
        }
    }

    private void removeVertex()
    {
        int vertex;
        vertex = getInt("Choose vertex: ");
        controller.removeVertex(vertex);
    }

    private void addVertex()
    {
        int vertex;
        vertex = getInt("Choose vertex: ");
        controller.addVertex(vertex);
    }

    private void removeEdge()
    {
        int vertex1;
        int vertex2;
        vertex1 = getInt("Choose vertex 1: ");
        vertex2 = getInt("Choose vertex 2: ");
        controller.removeEdge(vertex1, vertex2);
    }

    private void addEdge()
    {
        int vertex1;
        int vertex2;
        int cost;
        vertex1 = getInt("Choose vertex 1: ");
        vertex2 = getInt("Choose vertex 2: ");
        cost = getInt("Choose new cost: ");
        controller.addEdge(vertex1, vertex2, cost);
    }

    private void setEdgeCost()
    {
        int vertex1;
        int vertex2;
        int cost;
        vertex1 = getInt("Choose vertex 1: ");
        vertex2 = getInt("Choose vertex 2: ");
        cost = getInt("Choose new cost: ");
        controller.setCost(vertex1, vertex2, cost);
    }

    private void getEdgeCost()
    {
        int vertex1;
        int vertex2;
        vertex1 = getInt("Choose vertex 1: ");
        vertex2 = getInt("Choose vertex 2: ");
        print(controller.getCost(vertex1, vertex2));
    }

    private void outboundEdges()
    {
        int vertex;
        vertex = getInt("Choose vertex: ");
        print(controller.getOutEdges(vertex));
    }

    private void inboundEdges()
    {
        int vertex;
        vertex = getInt("Choose vertex: ");
        print(controller.getInEdges(vertex));
    }

    private void getOutDegree()
    {
        int vertex;
        vertex = getInt("Choose vertex: ");
        print(controller.getOutDegree(vertex));
    }

    private void getInDegree()
    {
        int vertex;
        vertex = getInt("Choose vertex: ");
        print(controller.getInDegree(vertex));
    }

    private void edgeExists()
    {
        int vertex1;
        int vertex2;
        vertex1 = getInt("Choose vertex 1: ");
        vertex2 = getInt("Choose vertex 2: ");
        print(controller.existsEdge(vertex1, vertex2));
    }

    private void listVertices()
    {
        print(controller.getVertices());
    }

    private void numberOfVertices()
    {
        print(controller.getNumberOfVertices());
    }

    private int getInt(String message)
    {
        int vertex1;
        print(message);
        vertex1 = scanner.nextInt();
        return vertex1;
    }

    private void print(String message)
    {
        System.out.println(message);
    }
}
