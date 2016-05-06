package MazeRep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * An implementation of a generic orthogonal maze using a graph.
 *
 * @author john
 */
public class GraphMaze<T> implements ExposedGraphMaze<T> {

    private Graph<T> graph;
    private Node<T> start;
    private Node<T> end;
    private int length;
    private int height;
    private Map<CoordinatePair, Node<T>> grid;

    /**
     * Constructs an empty GraphMaze with the specified dimensions
     *
     * @param height the unit height of the maze
     * @param length the unit length of the maze
     * @throws IllegalArgumentException if either of the dimensions specified are not greater than 0
     */
    public GraphMaze(int height, int length) {
        this.graph = null;
        this.start = null;
        this.end = null;
        this.grid = null;
        this.height = height;
        this.length = length;

        /* Parameter checking */
        if (height <= 0) {
            throw new IllegalArgumentException("Height of maze must be greater than 0");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Length of maze must be greater than 0");
        }

        /* Initialise empty graph */
        this.graph = new AdjacencyListGraph<>();

        /* Initialise empty grid */
        this.grid = new HashMap<>();

        /* Initialise nodes with nulls and populate grid and graph */
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                Node<T> node = new AdjacencyListGraphNode<>(null);
                this.grid.put(new CoordinatePair(i, j), node);
                this.graph.addNode(node);
            }
        }
    }

    /**
     * Returns the horizontal length of this GraphMaze, with each square being 1 unit.
     *
     * @return the horizontal length of this GraphMaze
     */
    @Override
    public int getLength() {
        return this.length;
    }

    /**
     * Returns the vertical height of this GraphMaze, with each square being 1 unit.
     *
     * @return the vertical height of this GraphMaze
     */
    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns the starting {@link MazeRep.Node} of this GraphMaze.
     *
     * @return the starting {@link MazeRep.Node} of this GraphMaze
     */
    @Override
    public Node<T> getStart() {
        return this.start;
    }

    /**
     * Returns the end {@link MazeRep.Node} of this GraphMaze.
     *
     * @return the end {@link MazeRep.Node} of this GraphMaze
     */
    @Override
    public Node<T> getEnd() {
        return this.end;
    }

    /**
     * Returns the {@link MazeRep.Node} in this GraphMaze representing the square at the given coordinates.
     *
     * @param down   the x-coordinate of the {@link MazeRep.Node}
     * @param across the y-coordinate of the {@link MazeRep.Node}
     * @return the {@link MazeRep.Node} in this GraphMaze representing the square at the given coordinates
     * @throws IndexOutOfBoundsException if the given coordinates are out of the range of this GraphMaze
     */
    @Override
    public Node<T> getNodeAt(int down, int across) {
        if (this.height < down) {
            throw new IndexOutOfBoundsException("GraphMaze has fewer rows than specified row " + down + ".");
        }
        if (this.length < across) {
            throw new IndexOutOfBoundsException("GraphMaze has fewer columns than specified column " + across + ".");
        }
        return this.grid.get(new CoordinatePair(down, across));
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
        return null;
    }

    /**
     * Returns a string representation of this GraphMaze.
     *
     * @return a string representation of this GraphMaze
     */
    @Override
    public String toString() {
        // TODO: figure out a nice way to do this
        return null;
    }

    /**
     * Creates and returns a deep copy of this GraphMaze.
     *
     * @return a deep copy of this GraphMaze
     */
    @Override
    public Maze<T> clone() {
        // TODO: deep copy
        return null;
    }

    /**
     * Returns the internal graph representation of this GraphMaze.
     *
     * @return the internal graph representation of this GraphMaze
     */
    @Override
    public Graph<T> getGraph() {
        return this.graph;
    }

    /**
     * Returns the internal grid representation of this GraphMaze.
     *
     * @return the internal grid representation of this GraphMaze
     */
    @Override
    public Map<CoordinatePair, Node<T>> getGrid() {
        return this.grid;
    }
}
