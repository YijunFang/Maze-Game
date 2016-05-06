package MazeRep.MazeGenStrategy;

import MazeRep.ExposedGraphMaze;

/**
 * An interface for strategies to generate the content of a {@link MazeRep.Maze} object.
 *
 * @author john
 */
public interface MazeGenStrategy<T> {
    /**
     * Generate the content of a maze.
     *
     * @param maze         the maze to generate the content for
     * @param defaultValue the default value of the grid squares of the maze
     */
    void generate(ExposedGraphMaze<T> maze, T defaultValue);
}
