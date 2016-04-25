package MazeRep;

import java.util.List;

/**
 * An interface for a generic data structure node.
 *
 * @author john
 */
public interface Node<T> {
    /**
     * Returns the value stored in this Node.
     *
     * @return the value stored in this Node
     */
    T getValue();

    /**
     * Sets the value stored in this Node.
     *
     * @param t the value to be stored in this Node
     * @return true if the value stored in this Node was successfully set
     */
    boolean setValue(T t);

    /**
     * Returns a list of Nodes that are adjacent to this Node.
     *
     * @return a list of adjacent Nodes
     */
    List<Node<T>> getNeighbours();

    /**
     * Returns a string representation of this Node.
     *
     * @return a string representation of this Node
     */
    String toString();

    /**
     * Creates and returns a deep copy of this Node.
     *
     * @return a deep copy of this Node
     */
    Node<T> clone();
}
