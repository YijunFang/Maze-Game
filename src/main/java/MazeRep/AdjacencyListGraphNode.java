package MazeRep;

/**
 * An implementation of a generic graph node.
 *
 * @author john
 */
public class AdjacencyListGraphNode<T> implements Node<T> {

    private T value;

    /**
     * Construct a new AdjacencyListGraphNode.
     *
     * @param value the value to be stored in this AdjacencyListGraphNode
     */
    public AdjacencyListGraphNode(T value) {
        this.value = value;
    }

    /**
     * Returns the value stored in this AdjacencyListGraphNode.
     *
     * @return the value stored in this AdjacencyListGraphNode
     */
    @Override
    public T getValue() {
        return this.value;
    }

    /**
     * Changes the value stored in this AdjacencyListGraphNode
     *
     * @param t the value to be stored in this AdjacencyListGraphNode
     * @return true if the specified value was successfully stored in this AdjacencyListGraphNode
     */
    @Override
    public boolean setValue(T t) {
        this.value = t;
        return true;
    }

    /**
     * Returns a string representation of this AdjacencyListGraphNode.
     *
     * @return a string representation of this AdjacencyListGraphNode
     */
    public String toString() {
        return (this.value == null) ? "(null)" : this.value.toString();
    }

    /**
     * Creates and returns a copy of this AdjacencyListGraphNode
     *
     * @return a copy of this AdjacencyListGraphNode
     */
    @Override
    public Node<T> clone() {
        return new AdjacencyListGraphNode<>(this.value);
    }
}
