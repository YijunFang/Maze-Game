package MazeRep;

import MazeRep.MazeGenStrategy.MazeGenStrategy;
import MazeRep.MazeGenStrategy.RandomisedRecursiveDFS;

/**
 * A factory for creating and initialising {@link MazeRep.Maze} objects with objects of type T as values in each square.
 *
 * @author john
 */
public class MazeFactory<T> {
    /**
     * Creates and initialises a new {@link MazeRep.Maze} object with the given parameters using the given maze generation strategy.
     *
     * @param mazeType     the concrete implementation of the {@link MazeRep.Maze} interface to use
     * @param height       the unit height of the {@link MazeRep.Maze}
     * @param length       the unit length of the {@link MazeRep.Maze}
     * @param defaultValue the default value of the grid square representations of the {@link MazeRep.Maze}
     * @param strategy     the {@link MazeRep.MazeGenStrategy.MazeGenStrategy} to use when generating the {@link MazeRep.Maze}
     * @return a new {@link MazeRep.Maze} object
     * @throws IllegalArgumentException if mazeType is not a concrete implementation of the {@link MazeRep.Maze} interface
     * @throws NullPointerException     if the given maze generation strategy is null
     * @throws IllegalStateException    if the generated maze was invalid for any reason
     */
    public Maze<T> generateMaze(Class mazeType, int height, int length, T defaultValue, MazeGenStrategy<T> strategy) {
        /* Construct empty maze using the relevant implementation */
        ExposedGraphMaze<T> maze = null;
        if (mazeType == GraphMaze.class) {
            maze = new GraphMaze<T>(height, length);
        } else {
            throw new IllegalArgumentException("Cannot generate a maze of type " + mazeType.getSimpleName());
        }

        /* Apply given strategy to generate maze content */
        if (strategy != null) {
            strategy.generate(maze, defaultValue);
        } else {
            throw new NullPointerException("Given maze generation strategy is null");
        }

        /* Choose a random start and end */
        /*
        Random rand = new Random();
        int startDown;
        int endDown;
        int startAcross;
        int endAcross;
        do {
            startDown = rand.nextInt(height);
            startAcross = rand.nextInt(length);
            endDown = rand.nextInt(height);
            endAcross = rand.nextInt(length);
        } while (startDown == endDown && startAcross == endAcross);
        if (!maze.setStart(maze.getNodeAt(startDown, startAcross))) {
            throw new IllegalStateException("Could not set start point of the maze");
        }
        if (!maze.setEnd(maze.getNodeAt(endDown, endAcross))) {
            throw new IllegalStateException("Could not set end point of the maze");
        }
        */

        /* Set start and end */
        maze.setStart(maze.getNodeAt(0, 0));
        maze.setEnd(maze.getNodeAt(maze.getHeight() - 1, maze.getLength() - 1));

        /* Validate maze */
        if (!this.validateMaze(maze)) {
            throw new IllegalStateException("Generated maze was invalid");
        }

        return maze;
    }

    /**
     * Creates and initialises a new {@link MazeRep.Maze} object with the given parameters using the default maze generation strategy.
     *
     * @param mazeType     the concrete implementation of the {@link MazeRep.Maze} interface to use
     * @param height       the unit height of the {@link MazeRep.Maze}
     * @param length       the unit length of the {@link MazeRep.Maze}
     * @param defaultValue the default value of the grid square representations of the {@link MazeRep.Maze}
     * @return a new {@link MazeRep.Maze} object
     * @throws IllegalArgumentException if mazeType is not a concrete implementation of the {@link MazeRep.Maze} interface
     * @throws IllegalStateException    if the generated maze was invalid for any reason
     */
    public Maze<T> generateMaze(Class mazeType, int height, int length, T defaultValue) {
        MazeGenStrategy<T> defaultStrategy = new RandomisedRecursiveDFS<>();
        return generateMaze(mazeType, height, length, defaultValue, defaultStrategy);
    }

    /**
     * Returns true if the given {@link MazeRep.Maze} is valid.
     * This means that the {@link MazeRep.Maze} can be navigated from start to finish.
     *
     * @param maze the maze to validate
     * @return true if the given {@link MazeRep.Maze} is valid
     */
    private boolean validateMaze(Maze<T> maze) {
        if (maze == null) {
            return false;
        }
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getLength(); j++) {
                if (maze.getNodeAt(i, j) != null) {
                    if (maze.getNodeAt(i, j).getValue() == null) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        if (maze.getStart() != null && maze.getEnd() != null) {
            if (maze.getShortestPath(maze.getStart(), maze.getEnd()) != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
