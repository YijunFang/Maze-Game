package MazeRep;

import Common.CoordinatePair;

import java.util.Map;

/**
 * An interface for a generic orthogonal maze implemented using a graph.
 * Exposes getters and setters for fields which are normally private to allow for factory access.
 *
 * @author john
 */
public interface ExposedGraphMaze<T> extends Maze<T> {
    /**
     * Returns the internal graph representation of this ExposedGraphMaze.
     *
     * @return the internal graph representation of this ExposedGraphMaze
     */
    Graph<T> getGraph();

    /**
     * Returns the internal grid representation of this ExposedGraphMaze.
     *
     * @return the internal grid representation of this ExposedGraphMaze
     */
    Map<CoordinatePair, Node<T>> getGrid();

    /**
     * Returns the internal inverse lookup grid representation of this ExposedGraphMaze.
     *
     * @return the internal inverse lookup grid representation of this ExposedGraphMaze
     */
    Map<Node<T>, CoordinatePair> getInverseGrid();

    /**
     * Sets the starting node of this ExposedGraphMaze.
     *
     * @param node the node to be set as the start of this ExposedGraphMaze
     * @return true if the specified node was successfully set as the start
     */
    boolean setStart(Node<T> node);

    /**
     * Sets the end node of this ExposedGraphMaze.
     *
     * @param node the node to be set as the end of this ExposedGraphMaze
     * @return true if the specified node was successfully set as the end
     */
    boolean setEnd(Node<T> node);
}
