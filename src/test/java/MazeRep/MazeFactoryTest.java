package MazeRep;

import Common.Content;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author john
 */
public class MazeFactoryTest {
    private MazeFactory<Content> contentMazeFactory;
    private MazeFactory<String> stringMazeFactory;

    @Before
    public void setUp() throws Exception {
        this.contentMazeFactory = new MazeFactory<>();
        this.stringMazeFactory = new MazeFactory<>();
    }

    @Test
    public void generateMaze() throws Exception {
        Maze<Content> contentMaze = contentMazeFactory.generateMaze(GraphMaze.class, 4, 4, Content.EMPTY);
        assertNotNull(contentMaze);
        assertEquals(4, contentMaze.getHeight());
        assertEquals(4, contentMaze.getLength());
        assertEquals(Content.EMPTY, contentMaze.getNodeAt(0, 0).getValue());
        assertEquals(Content.EMPTY, contentMaze.getNodeAt(3, 3).getValue());
        Maze<String> stringMaze = stringMazeFactory.generateMaze(GraphMaze.class, 2, 2, "foobar");
        assertNotNull(stringMaze);
        assertEquals(2, stringMaze.getHeight());
        assertEquals(2, stringMaze.getLength());
        assertEquals("foobar", stringMaze.getNodeAt(0, 0).getValue());
        assertEquals("foobar", stringMaze.getNodeAt(1, 1).getValue());
    }
}