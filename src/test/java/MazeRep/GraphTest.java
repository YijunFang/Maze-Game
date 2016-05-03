package MazeRep;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link MazeRep.Graph} interface.
 *
 * @author john
 */
public class GraphTest {

    private Graph<String> graph;
    private Node<String> node1;
    private Node<String> node2;

    @Before
    public void setUp() throws Exception {
        this.graph = new AdjacencyListGraph<String>();
        this.node1 = new AdjacencyListGraphNode<String>("foo");
        this.node2 = new AdjacencyListGraphNode<String>("bar");
    }

    @Test
    public void addNode() throws Exception {
        assertTrue(this.graph.addNode(this.node1));
        assertTrue(this.graph.hasNode(this.node1));
        assertFalse(this.graph.addNode(this.node1));
    }

    @Test
    public void hasNode() throws Exception {
        assertFalse(this.graph.hasNode(this.node1));
        assertFalse(this.graph.hasNode(this.node2));
    }

    @Test
    public void removeNode() throws Exception {
        assertTrue(this.graph.addNode(this.node1));
        assertTrue(this.graph.removeNode(this.node1));
        assertFalse(this.graph.hasNode(this.node1));
        assertFalse(this.graph.removeNode(this.node1));
    }

    @Test
    public void addEdge() throws Exception {
        /* Assert that a runtime exception is thrown when an edge is added with nodes that don't exist */
        try {
            this.graph.addEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.addNode(this.node1));
        try {
            this.graph.addEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.removeNode(this.node1));
        assertTrue(this.graph.addNode(this.node2));
        try {
            this.graph.addEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.addNode(this.node1));
        assertTrue(this.graph.hasNode(this.node1));
        assertTrue(this.graph.hasNode(this.node2));

        assertTrue(this.graph.addEdge(this.node1, this.node2));
        assertTrue(this.graph.hasEdge(this.node1, this.node2));
        assertFalse(this.graph.addEdge(this.node1, this.node2));
        assertFalse(this.graph.addEdge(this.node2, this.node1));

        assertTrue(this.graph.removeEdge(this.node1, this.node2));
        assertFalse(this.graph.hasEdge(this.node1, this.node2));

        assertTrue(this.graph.addEdge(this.node2, this.node1));
        assertTrue(this.graph.hasEdge(this.node1, this.node2));
        assertFalse(this.graph.addEdge(this.node1, this.node2));
        assertFalse(this.graph.addEdge(this.node2, this.node1));
    }

    @Test
    public void hasEdge() throws Exception {
        /* Assert that a runtime exception is thrown when an edge is queried with nodes that don't exist */
        try {
            this.graph.hasEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.addNode(this.node1));
        try {
            this.graph.hasEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.removeNode(this.node1));
        assertTrue(this.graph.addNode(this.node2));
        try {
            this.graph.hasEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.addNode(this.node1));
        assertTrue(this.graph.addEdge(this.node1, this.node2));

        assertTrue(this.graph.hasEdge(this.node1, this.node2));
        assertTrue(this.graph.hasEdge(this.node2, this.node1));

        assertTrue(this.graph.removeEdge(this.node1, this.node2));
        assertFalse(this.graph.hasEdge(this.node1, this.node2));
        assertFalse(this.graph.hasEdge(this.node2, this.node1));
    }

    @Test
    public void removeEdge() throws Exception {
        /* Assert that a runtime exception is thrown when an edge is queried with nodes that don't exist */
        try {
            this.graph.removeEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.addNode(this.node1));
        try {
            this.graph.removeEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.removeNode(this.node1));
        assertTrue(this.graph.addNode(this.node2));
        try {
            this.graph.removeEdge(this.node1, this.node2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        assertTrue(this.graph.addNode(this.node1));

        assertFalse(this.graph.removeEdge(this.node1, this.node2));
        assertFalse(this.graph.removeEdge(this.node2, this.node1));

        assertTrue(this.graph.addEdge(this.node1, this.node2));
        assertTrue(this.graph.hasEdge(this.node1, this.node2));

        assertTrue(this.graph.removeEdge(this.node1, this.node2));
        assertFalse(this.graph.hasEdge(this.node1, this.node2));

        assertTrue(this.graph.addEdge(this.node1, this.node2));
        assertTrue(this.graph.hasEdge(this.node1, this.node2));

        assertTrue(this.graph.removeEdge(this.node2, this.node1));
        assertFalse(this.graph.hasEdge(this.node1, this.node2));
    }

    @Test
    public void getNeighboursOf() throws Exception {
        fail("Test not implemented");
    }

    @Test
    public void getShortestPath() throws Exception {
        fail("Test not implemented");
    }

}