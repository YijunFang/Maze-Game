package MazeRep;

import java.util.Map;

/**
 * An interface for a generic orthogonal maze implemented using a graph.
 * Exposes getters for fields which are normally private to allow for factory access.
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
}
