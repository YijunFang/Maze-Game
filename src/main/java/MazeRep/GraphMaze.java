package MazeRep;

import java.util.List;

/**
 * @author john
 */
public class GraphMaze<T> implements Maze<T> {

    Graph<T> graph;
    Node<T> start;
    Node<T> end;

    public GraphMaze(int length, int height, int numCoins) {
        this.graph = null;
        this.start = null;
        this.end = null;

        // TODO: insert constructor method here
        // TODO: insert maze validation method here
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public Node<T> getStart() {
        return null;
    }

    @Override
    public Node<T> getEnd() {
        return null;
    }

    @Override
    public Node<T> getNodeAt(int x, int y) {
        return null;
    }

    @Override
    public List<Node<T>> getShortestPath(Node<T> from, Node<T> to) {
        return null;
    }

    @Override
    public Maze<T> clone() {
        return null;
    }
}
