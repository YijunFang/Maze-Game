package MazeRep;

import java.util.*;

/**
 * An implementation of a generic undirected unweighted graph using adjacency lists.
 *
 * @author john
 */
public class AdjacencyListGraph<T> implements Graph<T> {

    private Map<Node<T>, List<Node<T>>> nodes;

    public AdjacencyListGraph() {
        this.nodes = new HashMap<Node<T>, List<Node<T>>>();
    }

    public AdjacencyListGraph(List<Node<T>> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("AdjacencyListGraph cannot be initialised with a null node list.");
        }
        for (Node<T> node : nodes) {
            this.nodes.put(node, new LinkedList<Node<T>>());
        }
    }

    @Override
    public boolean addNode(Node<T> node) {
        if (this.hasNode(node)) {
            return false;
        } else {
            this.nodes.put(node, new LinkedList<Node<T>>());
            return true;
        }
    }

    @Override
    public boolean hasNode(Node<T> node) {
        return this.nodes.containsKey(node);
    }

    @Override
    public boolean removeNode(Node<T> node) {
        if (!this.hasNode(node)) {
            return false;
        } else {
            this.nodes.remove(node);
            return true;
        }
    }

    @Override
    public boolean addEdge(Node<T> node1, Node<T> node2) {
        if (!this.hasNode(node1) || !this.hasNode(node2)) {
            throw new IllegalArgumentException("Specified node does not exist in AdjacencyListGraph.");
        }
        if (!this.hasEdge(node1, node2)) {
            /* Add both edges */
            if (this.nodes.get(node1).add(node2)) {
                if (this.nodes.get(node2).add(node1)) {
                    return true;
                } else {
                    /* First edge was added, second edge was not */
                    /* Attempt to remove the edge that was added */
                    if (this.nodes.get(node1).remove(node2)) {
                        return false;
                    } else {
                        throw new IllegalStateException("Fatal error occurred whilst attempting to add edge.");
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean hasEdge(Node<T> node1, Node<T> node2) {
        if (!this.hasNode(node1) || !this.hasNode(node2)) {
            throw new IllegalArgumentException("Specified node does not exist in AdjacencyListGraph.");
        }
        if (this.nodes.get(node1).contains(node2)) {
            if (this.nodes.get(node2).contains(node1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeEdge(Node<T> node1, Node<T> node2) {
        if (!this.hasNode(node1) || !this.hasNode(node2)) {
            throw new IllegalArgumentException("Specified node does not exist in AdjacencyListGraph.");
        }
        if (this.hasEdge(node1, node2)) {
            /* Remove both edges */
            if (this.nodes.get(node1).remove(node2)) {
                if (this.nodes.get(node2).remove(node1)) {
                    return true;
                } else {
                    /* First edge was removed, second edge was not */
                    /* Attempt to restore edge that was removed */
                    if (this.nodes.get(node1).add(node2)) {
                        return false;
                    } else {
                        throw new IllegalStateException("Fatal error occurred whilst attempting to removed edge.");
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<Node<T>> getNeighboursOf(Node<T> node) {
        if (!this.hasNode(node)) {
            throw new IllegalArgumentException("Specified node does not exist in AdjacencyListGraph.");
        }
        return this.nodes.get(node);
    }

    @Override
    public List<Node<T>> getShortestPath(Node<T> from, Node<T> to) {
        // TODO: pathfinding algorithm
        return null;
    }

    @Override
    public Graph<T> clone() {
        // TODO: clone
        return null;
    }
}
