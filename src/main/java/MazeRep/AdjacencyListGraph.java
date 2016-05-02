package MazeRep;

import java.util.ArrayList;
import java.util.List;

/**
 * @author john
 */
public class AdjacencyListGraph<T> implements Graph<T> {

    private List<Node<T>> nodes;

    public AdjacencyListGraph() {
        this.nodes = new ArrayList<Node<T>>();
    }

    public AdjacencyListGraph(List<Node<T>> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("AdjacencyListGraph cannot be initialised with a null node list.");
        }
        this.nodes = nodes;
    }

    @Override
    public boolean addNode(Node<T> node) {
        return false;
    }

    @Override
    public boolean hasNode(Node<T> node) {
        return false;
    }

    @Override
    public boolean removeNode(Node<T> node) {
        return false;
    }

    @Override
    public boolean addEdge(Node<T> node1, Node<T> node2) {
        return false;
    }

    @Override
    public boolean hasEdge(Node<T> node1, Node<T> node2) {
        return false;
    }

    @Override
    public boolean removeEdge(Node<T> node1, Node<T> node2) {
        return false;
    }

    @Override
    public List<Node<T>> getShortestPath(Node<T> from, Node<T> to) {
        return null;
    }

    @Override
    public Graph<T> clone() {
        return null;
    }
}
