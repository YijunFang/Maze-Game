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

    /**
     * Adds a {@link MazeRep.Node} to this AdjacencyListGraph.
     *
     * @param node the {@link MazeRep.Node} to be added to this AdjacencyListGraph
     * @return true if the {@link MazeRep.Node} was successfully added to this AdjacencyListGraph
     */
    @Override
    public boolean addNode(Node<T> node) {
        if (this.hasNode(node)) {
            return false;
        } else {
            this.nodes.put(node, new LinkedList<Node<T>>());
            return true;
        }
    }

    /**
     * Returns true if a give {@link MazeRep.Node} exists in this AdjacencyListGraph.
     *
     * @param node the {@link MazeRep.Node} to check for existence
     * @return true if the {@link MazeRep.Node} exists in this AdjacencyListGraph
     */
    @Override
    public boolean hasNode(Node<T> node) {
        return this.nodes.containsKey(node);
    }

    /**
     * Removes a {@link MazeRep.Node} from this AdjacencyListGraph
     *
     * @param node the {@link MazeRep.Node} to remove from this AdjacencyListGraph
     * @return true if the {@link MazeRep.Node} was successfully removed from this AdjacencyListGraph
     */
    @Override
    public boolean removeNode(Node<T> node) {
        if (!this.hasNode(node)) {
            return false;
        } else {
            this.nodes.remove(node);
            return true;
        }
    }

    /**
     * Adds an edge between two given {@link MazeRep.Node}s in this AdjacencyListGraph.
     *
     * @param node1 one end {@link MazeRep.Node} of the edge to be added
     * @param node2 the other end {@link MazeRep.Node} of the edge to be added
     * @return true if an edge was successfully added between the two given {@link MazeRep.Node}s to the AdjacencyListGraph
     * @throws IllegalArgumentException if either of the given {@link MazeRep.Node}s do not exist in this AdjacencyListGraph
     * @throws IllegalStateException    if a fatal error occurs when adding the edge to this AdjacencyListGraph
     */
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

    /**
     * Returns true if an edge exists between two given {@link MazeRep.Node}s in this AdjacencyListGraph.
     *
     * @param node1 one end {@link MazeRep.Node} of the edge to check for existence
     * @param node2 the other end {@link MazeRep.Node} of the edge to check for existence
     * @return true if an edge exists between the two given {@link MazeRep.Node}s in this AdjacencyListGraph
     * @throws IllegalArgumentException if either of the given {@link MazeRep.Node}s do not exist in this AdjacencyListGraph
     */
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

    /**
     * Removes an edge between two given {@link MazeRep.Node}s in this AdjacencyListGraph.
     *
     * @param node1 one end {@link MazeRep.Node} of the edge to be removed
     * @param node2 the other end {@link MazeRep.Node} of the edge to be removed
     * @return true if an edge between the two given {@link MazeRep.Node}s was successfully removed from the AdjacencyListGraph
     * @throws IllegalArgumentException if either of the given {@link MazeRep.Node}s do not exist in this AdjacencyListGraph
     * @throws IllegalStateException    if a fatal error occurs when removing the edge from this AdjacencyListGraph
     */
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

    /**
     * Returns a list of {@link MazeRep.Node}s that are adjacent to the specified node in this AdjacencyListGraph.
     *
     * @param node the node to check for {@link MazeRep.Node}s adjacent to
     * @return a list of {@link MazeRep.Node}s adjacent to the specified node
     * @throws IllegalArgumentException if the given {@link MazeRep.Node} does not exist in this AdjacencyListGraph
     */
    @Override
    public List<Node<T>> getNeighboursOf(Node<T> node) {
        if (!this.hasNode(node)) {
            throw new IllegalArgumentException("Specified node does not exist in AdjacencyListGraph.");
        }
        return this.nodes.get(node);
    }

    /**
     * Finds the shortest path from a given starting {@link MazeRep.Node} and a given ending {@link MazeRep.Node}.
     *
     * @param from the starting {@link MazeRep.Node} of the path
     * @param to   the ending {@link MazeRep.Node} of the path
     * @return an ordered list of {@link MazeRep.Node}s representing the shortest path if it exists, otherwise null
     */
    @Override
    public List<Node<T>> getShortestPath(Node<T> from, Node<T> to) {
        // TODO: pathfinding algorithm
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a string representation of this AdjacencyListGraph.
     *
     * @return a string representation of this AdjacencyListGraph
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Node<T>, List<Node<T>>> entry : this.nodes.entrySet()) {
            stringBuilder.append("[").append(entry.getKey().toString()).append("]");
            stringBuilder.append(" :");
            for (Node<T> node : entry.getValue()) {
                stringBuilder.append(" ");
                stringBuilder.append(node.getValue().toString());
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Creates and returns a deep copy of this AdjacencyListGraph.
     *
     * @return a deep copy of this AdjacencyListGraph
     */
    @Override
    public Graph<T> clone() {
        // TODO: clone
        throw new UnsupportedOperationException();
    }
}
