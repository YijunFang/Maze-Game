package MazeRep;

import Common.CoordinatePair;
import MazeRep.MazeSearch.GraphMazeSearchState;
import MazeRep.MazeSearch.SearchState;

import java.util.*;

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
    private Map<Node<T>, CoordinatePair> inverseGrid;

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

        /* Initialise empty grids */
        this.grid = new HashMap<>();
        this.inverseGrid = new HashMap<>();

        /* Initialise nodes with nulls and populate grid and graph */
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                Node<T> node = new AdjacencyListGraphNode<>(null);
                CoordinatePair coordinatePair = new CoordinatePair(i, j);
                this.grid.put(coordinatePair, node);
                this.inverseGrid.put(node, coordinatePair);
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
        if (down >= this.height) {
            throw new IndexOutOfBoundsException("GraphMaze has fewer rows than specified row " + down + ".");
        } else if (down < 0) {
            throw new IndexOutOfBoundsException("Specified row number must be greater than 0.");
        }
        if (across >= this.length) {
            throw new IndexOutOfBoundsException("GraphMaze has fewer columns than specified column " + across + ".");
        } else if (across < 0) {
            throw new IndexOutOfBoundsException("Specified column number must be greater than 0.");
        }
        return this.grid.get(new CoordinatePair(down, across));
    }

    /**
     * Returns the {@link CoordinatePair} reference of the given node representation of a square in this Maze.
     *
     * @param node a {@link Node} in this Maze
     * @return the {@link CoordinatePair} reference of the given node representation of a square in this Maze
     * @throws IllegalArgumentException if the given {@link Node} does not exist in this Maze
     */
    @Override
    public CoordinatePair getCoordinatesOf(Node<T> node) {
        if (this.graph.hasNode(node)) {
            return this.inverseGrid.get(node);
        } else {
            throw new IllegalArgumentException("Given node does not exist in this Maze");
        }
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
        /* Initialise A* search data structures */
        CoordinatePair startCoordinatePair = null;
        CoordinatePair goalCoordinatePair = null;
        for (Map.Entry<CoordinatePair, Node<T>> entry : this.grid.entrySet()) {
            if (entry.getValue().equals(from)) {
                startCoordinatePair = entry.getKey();
            }
            if (entry.getValue().equals(to)) {
                goalCoordinatePair = entry.getKey();
            }
            if (startCoordinatePair != null && goalCoordinatePair != null) {
                break;
            }
        }
        if (startCoordinatePair == null) {
            throw new IllegalArgumentException("Start node does not exist in maze");
        } else if (goalCoordinatePair == null) {
            throw new IllegalArgumentException("Goal node does not exist in maze");
        }
        Map<CoordinatePair, Boolean> visited = new HashMap<>();
        for (CoordinatePair coordinatePair : this.grid.keySet()) {
            visited.put(coordinatePair, false);
        }
        PriorityQueue<SearchState<CoordinatePair>> pq = new PriorityQueue<>();
        SearchState<CoordinatePair> currState = null;
        boolean completed = false;

        /* Add the starting state to the priority queue */
        pq.add(new GraphMazeSearchState(startCoordinatePair, goalCoordinatePair, 0, null));

        /* While there are elements on the priority queue */
        while (!pq.isEmpty()) {
            /* Pop the element with the lowest cost + heuristic value off of the priority queue */
            currState = pq.poll();
            if (currState == null) {
                throw new NullPointerException("Null was popped off of the priority queue");
            }
            CoordinatePair currCoordinatePair = currState.getValue();
            /* If element is the goal, stop */
            if (currState.getValue().equals(goalCoordinatePair)) {
                completed = true;
                break;
            }
            /* If element has been visited, ignore it */
            if (visited.get(currState.getValue())) {
                continue;
            }
            /* Mark element as visited */
            visited.put(currState.getValue(), true);
            /* Add child elements to priority queue */
            /* Get all adjacent cells */
            List<CoordinatePair> neighbours = new ArrayList<>(4);
            /* Checking above */
            try {
                this.getNodeAt(currCoordinatePair.down - 1, currCoordinatePair.across);
                neighbours.add(new CoordinatePair(currCoordinatePair.down - 1, currCoordinatePair.across));
            } catch (IndexOutOfBoundsException e) {
                /* No cell above current cell */
            }
            /* Checking to the right */
            try {
                this.getNodeAt(currCoordinatePair.down, currCoordinatePair.across - 1);
                neighbours.add(new CoordinatePair(currCoordinatePair.down, currCoordinatePair.across - 1));
            } catch (IndexOutOfBoundsException e) {
                /* No cell to the right of current cell */
            }
            /* Checking below */
            try {
                this.getNodeAt(currCoordinatePair.down + 1, currCoordinatePair.across);
                neighbours.add(new CoordinatePair(currCoordinatePair.down + 1, currCoordinatePair.across));
            } catch (IndexOutOfBoundsException e) {
                /* No cell below current cell */
            }
            /* Checking to the left */
            try {
                this.getNodeAt(currCoordinatePair.down, currCoordinatePair.across + 1);
                neighbours.add(new CoordinatePair(currCoordinatePair.down, currCoordinatePair.across + 1));
            } catch (IndexOutOfBoundsException e) {
                /* No cell to the left of current cell */
            }
            for (CoordinatePair coordinatePair : neighbours) {
                /* Add only connected adjacent cells to the priority queue */
                if (this.graph.hasEdge(
                        this.getNodeAt(currCoordinatePair.down, currCoordinatePair.across),
                        this.getNodeAt(coordinatePair.down, coordinatePair.across)
                )) {
                    pq.add(new GraphMazeSearchState(coordinatePair, goalCoordinatePair, currState.getCost() + 1, currState));
                }
            }
        }

        if (!completed) {
            /* Search exited abnormally */
            throw new IllegalStateException("Search aborted abnormally");
        }

        List<CoordinatePair> coordinatePairPath = currState.constructPath();
        List<Node<T>> nodePath = new LinkedList<>();
        for (CoordinatePair coordinatePair : coordinatePairPath) {
            nodePath.add(this.getNodeAt(coordinatePair.down, coordinatePair.across));
        }

        return nodePath;
    }

    /**
     * Returns true if the given {@link MazeRep.Node}s are adjacent to each other.
     * @param node1 the first {@link MazeRep.Node}
     * @param node2 the second {@link MazeRep.Node}
     * @return true if the given {@link MazeRep.Node}s are adjacent to each other
     * @throws IllegalArgumentException if either of the given {@link MazeRep.Node}s do not exist in this Maze
     */
    @Override
    public boolean areAdjacent(Node<T> node1, Node<T> node2) {
        return this.graph.hasEdge(node1, node2);
    }

    /**
     * Returns a string representation of this GraphMaze.
     *
     * @return a string representation of this GraphMaze
     */
    @Override
    public String toString() {
        // TODO: figure out a nice way to do this
        throw new UnsupportedOperationException();
    }

    /**
     * Creates and returns a deep copy of this GraphMaze.
     *
     * @return a deep copy of this GraphMaze
     */
    @Override
    public Maze<T> clone() {
        // TODO: deep copy
        throw new UnsupportedOperationException();
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

    /**
     * Returns the internal inverse lookup grid representation of this GraphMaze.
     *
     * @return the internal inverse lookup grid representation of this GraphMaze
     */
    @Override
    public Map<Node<T>, CoordinatePair> getInverseGrid() {
        return this.inverseGrid;
    }

    /**
     * Sets the starting node of this GraphMaze.
     *
     * @param node the node to be set as the start of this GraphMaze
     * @return true if the node was successfully set as the starting node
     */
    @Override
    public boolean setStart(Node<T> node) {
        if (this.graph.hasNode(node)) {
            this.start = node;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the end node of this GraphMaze.
     *
     * @param node the node to be set as the end of this GraphMaze
     * @return true if the node was successfully set as the end node
     */
    @Override
    public boolean setEnd(Node<T> node) {
        if (this.graph.hasNode(node)) {
            this.end = node;
            return true;
        } else {
            return false;
        }
    }
}
