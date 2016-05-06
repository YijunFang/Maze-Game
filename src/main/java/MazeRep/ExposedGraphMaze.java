package MazeRep;

import java.util.Map;

/**
 * An interface for a generic orthogonal maze implemented using a graph.
 * Exposes fields which are normally private to allow for factory access.
 *
 * @author john
 */
public interface ExposedGraphMaze<T> extends Maze<T> {
    Graph<T> getGraph();
    Map<CoordinatePair, Node<T>> getGrid();
}
