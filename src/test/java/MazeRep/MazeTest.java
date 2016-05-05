package MazeRep;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link MazeRep.Maze} interface.
 *
 * Created by john on 5/5/16.
 */
public class MazeTest {

    private Maze<String> maze;

    @Before
    public void setUp() throws Exception {
        this.maze = new GraphMaze<>(3, 2, 0);
    }

    @Test
    public void getLength() throws Exception {
        assertEquals(2, this.maze.getLength());
    }

    @Test
    public void getHeight() throws Exception {
        assertEquals(3, this.maze.getHeight());
    }

    @Test
    public void getStart() throws Exception {
        assertNull(this.maze.getStart());
        // TODO: implement test after maze can be generated
        fail("Test not implemented.");
    }

    @Test
    public void getEnd() throws Exception {
        assertNull(this.maze.getEnd());
        // TODO: implement test after maze can be generated
        fail("Test not implemented.");
    }

    @Test
    public void getNodeAt() throws Exception {
        assertNull(this.maze.getNodeAt(0, 0));
        assertNull(this.maze.getNodeAt(0, 1));
        assertNull(this.maze.getNodeAt(1, 0));
        assertNull(this.maze.getNodeAt(1, 1));
        try {
            this.maze.getNodeAt(-1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        try {
            this.maze.getNodeAt(0, -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        try {
            this.maze.getNodeAt(-1, -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        try {
            this.maze.getNodeAt(2, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        try {
            this.maze.getNodeAt(0, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        try {
            this.maze.getNodeAt(2, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            //
        }
        // TODO: implement test after maze can be generated
        fail("Test not implemented.");
    }

    @Test
    public void getShortestPath() throws Exception {
        // TODO: implement test after maze can be generated and pathfinding algorithm implemented
        fail("Test not implemented.");
    }

}