package MazeRep.MazeGenStrategy;

import Common.CoordinatePair;
import MazeRep.ExposedGraphMaze;

import java.util.*;

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
        Random random = new Random();
        CoordinatePair currCoordinatePair;
        Stack<CoordinatePair> stack = new Stack<>();
        HashMap<CoordinatePair, Boolean> visited = new HashMap<>();
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getLength(); j++) {
                visited.put(new CoordinatePair(i, j), false);
            }
        }

        /* Choose random cell to start */
        int randomDown = random.nextInt(maze.getHeight());
        int randomAcross = random.nextInt(maze.getLength());
        currCoordinatePair = new CoordinatePair(randomDown, randomAcross);
        visited.put(currCoordinatePair, true);
        /* Push starting cell coordinates to stack */
        stack.push(currCoordinatePair);
        /* While there are unvisited cells */
        while (visited.containsValue(false) || !stack.isEmpty()) {
            /* Initialise current cell */
            maze.getNodeAt(currCoordinatePair.down, currCoordinatePair.across).setValue(defaultValue);
            /* Check for unvisited cells neighbouring the current cell */
            List<CoordinatePair> neighbours = new ArrayList<>(4);
            /* Checking above */
            try {
                maze.getNodeAt(currCoordinatePair.down - 1, currCoordinatePair.across);
                neighbours.add(new CoordinatePair(currCoordinatePair.down - 1, currCoordinatePair.across));
            } catch (IndexOutOfBoundsException e) {
                /* No cell above current cell */
            }
            /* Checking to the right */
            try {
                maze.getNodeAt(currCoordinatePair.down, currCoordinatePair.across - 1);
                neighbours.add(new CoordinatePair(currCoordinatePair.down, currCoordinatePair.across - 1));
            } catch (IndexOutOfBoundsException e) {
                /* No cell to the right of current cell */
            }
            /* Checking below */
            try {
                maze.getNodeAt(currCoordinatePair.down + 1, currCoordinatePair.across);
                neighbours.add(new CoordinatePair(currCoordinatePair.down + 1, currCoordinatePair.across));
            } catch (IndexOutOfBoundsException e) {
                /* No cell below current cell */
            }
            /* Checking to the left */
            try {
                maze.getNodeAt(currCoordinatePair.down, currCoordinatePair.across + 1);
                neighbours.add(new CoordinatePair(currCoordinatePair.down, currCoordinatePair.across + 1));
            } catch (IndexOutOfBoundsException e) {
                /* No cell to the left of current cell */
            }
            List<CoordinatePair> unvisitedNeighbours = new ArrayList<>(4);
            for (CoordinatePair neighbourCoordinatePair : neighbours) {
                if (!visited.get(neighbourCoordinatePair)) {
                    unvisitedNeighbours.add(neighbourCoordinatePair);
                }
            }
            if (!unvisitedNeighbours.isEmpty()) {
                /* If the current cell has any unvisited neighbours */
                /* Randomly choose one of the unvisited neighbours */
                CoordinatePair chosenCoordinatePair = unvisitedNeighbours.get(random.nextInt(unvisitedNeighbours.size()));
                /* Push the current cell to the stack */
                stack.push(currCoordinatePair);
                /* Connect the current cell to the chosen cell */
                maze.getGraph().addEdge(
                        maze.getNodeAt(currCoordinatePair.down, currCoordinatePair.across),
                        maze.getNodeAt(chosenCoordinatePair.down, chosenCoordinatePair.across)
                );
                /* Make the chosen cell the current cell and mark it as visited */
                currCoordinatePair = chosenCoordinatePair;
                visited.put(chosenCoordinatePair, true);
            } else if (!stack.isEmpty()) {
                /* Otherwise, if the stack is empty */
                /* Pop a cell from the stack and make it the current cell */
                currCoordinatePair = stack.pop();
            }
        }
    }
}
