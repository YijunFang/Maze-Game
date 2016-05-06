package MazeRep.MazeGenStrategy;

import MazeRep.CoordinatePair;
import MazeRep.ExposedGraphMaze;

import java.util.HashMap;
import java.util.Stack;

/**
 * An implementation of a maze generation strategy using randomised recursive depth-first search.
 *
 * @author john
 */
public class RandomisedRecursiveDFS<T> implements MazeGenStrategy<T> {
    /**
     * Generate the content of a maze, using an iteration emulation of randomised recursive depth-first search.
     *
     * @param maze         the maze to generate the content for
     * @param defaultValue the default value of the grid squares of the maze
     */
    @Override
    public void generate(ExposedGraphMaze<T> maze, T defaultValue) {
        /* Initialise maze generation data structures */
        CoordinatePair currCoordinatePair;
        Stack<CoordinatePair> stack = new Stack<>();
        HashMap<CoordinatePair, Boolean> visited = new HashMap<>();
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getLength(); j++) {
                visited.put(new CoordinatePair(i, j), false);
            }
        }

        /* Choose random cell to start */
        currCoordinatePair = null; // TODO random start position
        visited.put(currCoordinatePair, true);
        /* Push starting cell coordinates to stack */
        stack.push(currCoordinatePair);
        /* While there are univisited cells */
        while (visited.containsValue(false)) {
            if (false) {
                /* If the current cell has any unvisited neighbours */
                /* Randomly choose one of the unvisited neighbours */
                CoordinatePair chosenCoordinatePair = null; // TODO choose neighbour
                /* Push the current cell to the stack */
                stack.push(currCoordinatePair);
                /* Connect the current cell to the chosen cell */
                maze.getGraph().addEdge(
                        maze.getNodeAt(currCoordinatePair.down, currCoordinatePair.across),
                        maze.getNodeAt(chosenCoordinatePair.down, chosenCoordinatePair.across)
                );
                /* Make the chosen cell the current cell and mark it as visited */
                currCoordinatePair = chosenCoordinatePair;
            } else if (!stack.isEmpty()) {
                /* Otherwise, if the stack is empty */
                /* Pop a cell from the stack and make it the current cell */
                currCoordinatePair = stack.pop();
            }
        }
    }
}
