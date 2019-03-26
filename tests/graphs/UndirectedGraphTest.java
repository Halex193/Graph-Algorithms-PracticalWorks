package graphs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class UndirectedGraphTest
{
    private UndirectedGraph graph;
    @Before
    public void setUp() throws Exception
    {
        graph = new UndirectedGraph(5, 6);
        graph.addEdge(0,1,7);
        graph.addEdge(1,2,2);
        graph.addEdge(1,3,8);
        graph.addEdge(2,3,5);
    }

    @After
    public void tearDown() throws Exception
    {
        graph = null;
    }

    @Test
    public void inDegree()
    {
        assertEquals(graph.degree(0), 1);
        assertEquals(graph.degree(1), 3);
        assertEquals(graph.degree(2), 2);
        assertEquals(graph.degree(3), 2);
        assertEquals(graph.degree(4), 0);
    }

    @Test
    public void parseAdjacentEdges()
    {
        Iterator<Integer> iterator = graph.parseAdjacentEdges(3).iterator();
        assertEquals((int)iterator.next(), 1);
        assertEquals((int)iterator.next(), 2);

        iterator = graph.parseAdjacentEdges(2).iterator();
        assertEquals((int)iterator.next(), 1);
        assertEquals((int)iterator.next(), 3);
    }

    @Test
    public void getNumberOfEdges()
    {
        assertEquals(graph.getNumberOfEdges(), 4);
        graph.addEdge(0, 4, 5);
        assertEquals(graph.getNumberOfEdges(), 5);
    }

    @Test
    public void parseVertices()
    {
        Iterator<Integer> iterator = graph.parseVertices().iterator();
        int i = 0;
        while (iterator.hasNext())
        {
            assertEquals((int)iterator.next(), i++);
        }
    }

    @Test
    public void existsEdge()
    {
        assertTrue(graph.existsEdge(1, 2));
        assertTrue(graph.existsEdge(2, 1));
        assertFalse(graph.existsEdge(4, 3));
    }

    @Test
    public void getCost()
    {
        assertEquals(graph.getCost(2, 1), 2);
    }

    @Test
    public void changeCost()
    {
        assertEquals(graph.getCost(0, 1), 7);
        graph.changeCost(0,1,14);
        assertEquals(graph.getCost(0, 1), 14);
    }

    @Test
    public void addVertex()
    {
        assertFalse(graph.existsVertex(15));
        graph.addVertex(15);
        assertTrue(graph.existsVertex(15));
    }

    @Test
    public void existsVertex()
    {
        for (int i = 0; i < 5; i++)
        {
            assertTrue(graph.existsVertex(i));
        }
    }

    @Test
    public void removeVertex()
    {
        assertTrue(graph.existsVertex(3));
        graph.removeVertex(3);
        assertFalse(graph.existsVertex(3));
        assertFalse(graph.existsEdge(2, 3));
        assertFalse(graph.existsEdge(1, 3));
    }

    @Test
    public void addEdge()
    {
        assertEquals(graph.getNumberOfEdges(), 4);
        graph.addEdge(0, 4, 5);
        assertEquals(graph.getNumberOfEdges(), 5);
        assertEquals(graph.degree(4), 1);
    }

    @Test
    public void removeEdge()
    {
        graph.removeEdge(0,1);
        assertFalse(graph.existsEdge(0,1));
        assertEquals(graph.degree(0), 0);
    }

    @Test
    public void copy()
    {
        UndirectedGraph newGraph = graph.copy();
        assertEquals(newGraph.getNumberOfEdges(), 4);
    }
}