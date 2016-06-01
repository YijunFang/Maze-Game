package MazeRep;

import Common.CoordinatePair;

import java.util.List;

/**
 * An interface for a generic orthogonal maze.
 *
 * @author john
 */
public interface Maze<T> {
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
     * Returns the {@link MazeRep.Node} in this Maze representing the square at the given coordinates.
     *
     * @param down the x-coordinate of the {@link MazeRep.Node}
     * @param across the y-coordinate of the {@link MazeRep.Node}
     * @return the {@link MazeRep.Node} in this Maze representing the square at the given coordinates
     * @throws IndexOutOfBoundsException if the given coordinates are out of the range of this Maze
     */
    Node<T> getNodeAt(int down, int across);

    /**
     * Returns the {@link CoordinatePair} reference of the given node representation of a square in this Maze.
     *
     * @param node a {@link Node} in this Maze
     * @return the {@link CoordinatePair} reference of the given node representation of a square in this Maze
     * @throws IllegalArgumentException if the given {@link Node} does not exist in this Maze
     */
    CoordinatePair getCoordinatesOf(Node<T> node);

    /**
     * Finds the shortest path from a given starting {@link MazeRep.Node} and a given ending {@link MazeRep.Node}.
     *
     * @param from the starting {@link MazeRep.Node} of the path
     * @param to the ending {@link MazeRep.Node} of the path
     * @return an ordered list of {@link MazeRep.Node}s representing the shortest path if it exists, otherwise null
     */
    List<Node<T>> getShortestPath(Node<T> from, Node<T> to);

    /**
     * Returns true if the given {@link MazeRep.Node}s are adjacent to each other.
     * @param node1 the first {@link MazeRep.Node}
     * @param node2 the second {@link MazeRep.Node}
     * @return true if the given {@link MazeRep.Node}s are adjacent to each other
     */
    boolean areAdjacent(Node<T> node1, Node<T> node2);

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
