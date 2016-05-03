package MazeRep;

import java.util.List;

/**
 * An interface for a generic undirected unweighted graph of {@link MazeRep.Node}s.
 *
 * @author john
 */
public interface Graph<T> {
    /**
     * Adds a {@link MazeRep.Node} to this Graph.
     *
     * @param node the {@link MazeRep.Node} to be added to this Graph
     * @return true if the {@link MazeRep.Node} was successfully added to this Graph
     */
    boolean addNode(Node<T> node);

    /**
     * Returns true if a given {@link MazeRep.Node} exists in this Graph.
     *
     * @param node the {@link MazeRep.Node} to check for existence
     * @return true if the {@link MazeRep.Node} exists in this Graph
     */
    boolean hasNode(Node<T> node);

    /**
     * Removes a {@link MazeRep.Node} from this Graph.
     *
     * @param node the {@link MazeRep.Node} to remove from this Graph
     * @return true if the {@link MazeRep.Node} was successfully removed from this Graph
     */
    boolean removeNode(Node<T> node);

    /**
     * Adds an edge between two given {@link MazeRep.Node}s in this Graph.
     *
     * @param node1 one end {@link MazeRep.Node} of the edge to be added
     * @param node2 the other end {@link MazeRep.Node} of the edge to be added
     * @return true if an edge was successfully added between the two given {@link MazeRep.Node}s to this Graph
     * @throws IllegalArgumentException if either of the given {@link MazeRep.Node}s do not exist in this Graph
     * @throws IllegalStateException    if a fatal error occurs when adding the edge to this Graph
     */
    boolean addEdge(Node<T> node1, Node<T> node2);

    /**
     * Returns true if an edge exists between two given {@link MazeRep.Node}s in this Graph.
     *
     * @param node1 one end {@link MazeRep.Node} of the edge to check for existence
     * @param node2 the other end {@link MazeRep.Node} of the edge to check for existence
     * @return true if an edge exists between the two given {@link MazeRep.Node}s in this Graph
     * @throws IllegalArgumentException if either of the given {@link MazeRep.Node}s do not exist in this Graph
     */
    boolean hasEdge(Node<T> node1, Node<T> node2);

    /**
     * Removes an edge between two given {@link MazeRep.Node}s in this Graph.
     *
     * @param node1 one end {@link MazeRep.Node} of the edge to be removed
     * @param node2 the other end {@link MazeRep.Node} of the edge to be removed
     * @return true if an edge between the two given {@link MazeRep.Node}s was successfully removed from this Graph
     * @throws IllegalArgumentException if either of the given {@link MazeRep.Node}s do not exist in this Graph
     * @throws IllegalStateException    if a fatal error occurs when removing the edge from this Graph
     */
    boolean removeEdge(Node<T> node1, Node<T> node2);

    /**
     * Returns a list of {@link MazeRep.Node}s that are adjacent to the specified node in this Graph.
     *
     * @param node the node to check for {@link MazeRep.Node}s adjacent to
     * @return a list of {@link MazeRep.Node}s adjacent to the specified node
     * @throws IllegalArgumentException if the given {@link MazeRep.Node} does not exist in this Graph
     */
    List<Node<T>> getNeighboursOf(Node<T> node);

    /**
     * Finds the shortest path from a given starting {@link MazeRep.Node} and a given ending {@link MazeRep.Node}.
     *
     * @param from the starting {@link MazeRep.Node} of the path
     * @param to   the ending {@link MazeRep.Node} of the path
     * @return an ordered list of {@link MazeRep.Node}s representing the shortest path if it exists, otherwise null
     */
    List<Node<T>> getShortestPath(Node<T> from, Node<T> to);

    /**
     * Returns a string representation of this Graph.
     *
     * @return a string representation of this Graph
     */
    String toString();

    /**
     * Creates and returns a deep copy of this Graph.
     *
     * @return a deep copy of this Graph
     */
    Graph<T> clone();
}
