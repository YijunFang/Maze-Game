package MazeRep;

/**
 * An interface for an orthogonal maze implemented using a {@link MazeRep.Graph}.
 *
 * @author john
 */
public interface Maze<T> extends Graph<T> {
    /**
     * Returns the horizontal length of this Maze, with each square being 1 unit.
     *
     * @return the horizontal length of this Maze
     */
    int getLength();

    /**
     * Returns the vertical height of this Maze, with each square being 1 unit.
     *
     * @return the vertical height of this Maze
     */
    int getHeight();

    /**
     * Returns the starting {@link MazeRep.Node} of this Maze.
     *
     * @return the starting {@link MazeRep.Node} of this Maze
     */
    Node<T> getStart();

    /**
     * Returns the end {@link MazeRep.Node} of this Maze.
     *
     * @return the end {@link MazeRep.Node} of this Maze
     */
    Node<T> getEnd();

    /**
     * Returns a string representation of this Maze.
     *
     * @return a string representation of this Maze
     */
    String toString();

    /**
     * Creates and returns a deep copy of this Maze.
     *
     * @return a deep copy of this Maze
     */
    Maze<T> clone();
}
