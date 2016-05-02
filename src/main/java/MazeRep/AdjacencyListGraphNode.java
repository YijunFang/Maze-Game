package MazeRep;

import java.util.List;

/**
 * @author john
 */
public class AdjacencyListGraphNode<T> implements Node<T> {

    private T value;
    private List<Node<T>> adjacentNodes;

    public AdjacencyListGraphNode(T value) {
        this.value = value;
    }

    public AdjacencyListGraphNode(T value, List<Node<T>> adjacentNodes) {
        if (adjacentNodes == null) {
            throw new IllegalArgumentException("AdjacencyListGraphNode cannot be initialised with a null adjacency list.");
        }
        this.value = value;
        this.adjacentNodes = adjacentNodes;
    }

    @Override
    public T getValue() {
        return null;
    }

    @Override
    public boolean setValue(T t) {
        return false;
    }

    @Override
    public List<Node<T>> getNeighbours() {
        return null;
    }

    @Override
    public Node<T> clone() {
        return null;
    }
}
