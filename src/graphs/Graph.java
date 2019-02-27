package graphs;

import java.util.Iterator;

public interface Graph
{

    int getNumberOfVertices();

    int getNumberOfEdges();

    Iterator<Integer> parseVertices();

    boolean existsEdge(int vertex1, int vertex2);

    int getCost(int vertex1, int vertex2);

    void changeCost(int vertex1, int vertex2, int newCost);

    void addVertex(int vertex);

    void removeVertex(int vertex);

    void addEdge(int vertex1, int vertex2, int cost);

    void removeEdge(int vertex1, int vertex2);

    Graph copy();
}
