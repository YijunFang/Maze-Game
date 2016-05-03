package MazeRep;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link MazeRep.Node} interface.
 *
 * @author john
 */
public class NodeTest {

    private Node<String> node;
    private String value1;
    private String value2;

    @Before
    public void setUp() throws Exception {
        this.value1 = "foo";
        this.value2 = "bar";
        this.node = new AdjacencyListGraphNode<>(value1);
    }

    @Test
    public void getValue() throws Exception {
        assertEquals(this.value1, this.node.getValue());
        assertEquals("foo", this.node.getValue());
    }

    @Test
    public void setValue() throws Exception {
        assertTrue(this.node.setValue(value2));
        assertEquals(this.value2, this.node.getValue());
    }

}