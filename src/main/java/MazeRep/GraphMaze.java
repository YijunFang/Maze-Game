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
public class GraphMaze<T> implements Maze<T> {

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

    public void generateMaze(T defaultValue) {
        /* Generate maze */
        GraphMazeGenerator<T> graphMazeGenerator = new GraphMazeGenerator<>(this, defaultValue);
        graphMazeGenerator.generateMaze();

        /* Validate maze */
        // TODO
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

    private class CoordinatePair {
        public final int down;
        public final int across;

        public CoordinatePair(int down, int across) {
            this.down = down;
            this.across = across;
        }

        @Override
        public int hashCode() {
            return this.down ^ this.across;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            } else {
                CoordinatePair castedObj = (CoordinatePair)obj;
                return (castedObj.down == this.down && castedObj.across == this.across);
            }
        }
    }

    private class GraphMazeGenerator<T> {
        private GraphMaze<T> graphMaze;
        private T defaultValue;
        private Stack<CoordinatePair> stack;
        private Map<CoordinatePair, Boolean> visited;

        public GraphMazeGenerator(GraphMaze<T> graphMaze, T defaultValue) {
            this.graphMaze = graphMaze;
            this.defaultValue = defaultValue;
            this.stack = new Stack<>();
            this.visited = new HashMap<>();
            for (int i = 0; i < this.graphMaze.height; i++) {
                for (int j = 0; j < this.graphMaze.length; j++) {
                    this.visited.put(new CoordinatePair(i, j), false);
                }
            }
        }

        public void generateMaze() {
            /* Choose random edge node to start */
            CoordinatePair currCoordinatePair = null; // TODO random start position
            this.visited.put(currCoordinatePair, true);
            /* Push starting node coordinates to stack */
            this.stack.push(currCoordinatePair);
            /* While there are unvisited cells */
            while (this.visited.containsValue(false)) {
                if (false) { // TODO: condition
                    /* If the current cell has any unvisited neighbours */
                    /* Choose randomly one of the unvisited neighbours */
                    CoordinatePair chosenCoordinatePair = null; // TODO choose neighbour
                    /* Push the current cell to the stack */
                    this.stack.push(currCoordinatePair);
                    /* Connect the current cell to the chosen cell */
                    this.graphMaze.graph.addEdge(
                            this.graphMaze.getNodeAt(currCoordinatePair.down, currCoordinatePair.across),
                            this.graphMaze.getNodeAt(chosenCoordinatePair.down, chosenCoordinatePair.across));
                    /* Make the chosen cell the current cell and mark it as visited */
                    currCoordinatePair = chosenCoordinatePair;
                    this.visited.put(currCoordinatePair, true);
                } else if (!this.stack.isEmpty()) {
                    /* Else if the stack is not empty */
                    /* Pop a cell from the stack and make it the current cell */
                    currCoordinatePair = stack.pop();
                }
            }
        }
    }
}
