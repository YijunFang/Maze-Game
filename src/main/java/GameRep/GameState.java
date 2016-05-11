package GameRep;

import Common.Content;
import Common.CoordinatePair;
import Common.Difficulty;
import MazeRep.GraphMaze;
import MazeRep.Maze;
import MazeRep.MazeFactory;
import MazeRep.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * The representation of the maze game at the current time.
 *
 * @author john
 */
public class GameState {

    private Maze<Content> maze;
    private Difficulty difficultyLevel;
    private CoordinatePair playerPosition;
    private int numOfCoins;

    /**
     * Creates a new game with the given difficulty level.
     *
     * @param difficultyLevel the difficulty level of the game
     */
    public GameState(Difficulty difficultyLevel) {
        MazeFactory<Content> mazeFactory = new MazeFactory<>();
        this.maze = mazeFactory.generateMaze(GraphMaze.class,
                difficultyLevel.getSideLength(),
                difficultyLevel.getSideLength(),
                Content.EMPTY);
        this.playerPosition = this.maze.getCoordinatesOf(this.maze.getStart());
        this.difficultyLevel = difficultyLevel;
        this.numOfCoins = 0;
        /* this.numOfCoins = difficultyLevel.getNumberOfCoins(); */ //TODO use when coins are actually placed
    }

    /**
     * Returns the difficult level of this game.
     *
     * @return the difficulty level of this game
     */
    public Difficulty getDifficultyLevel() {
        return this.difficultyLevel;
    }

    /**
     * Returns the {@link Square} at the given coordinates.
     *
     * @param coordinatePair the coordinates of the {@link Square} to return
     * @return the {@link Square} at the given coordinates
     * @throws IllegalArgumentException if the given coordinates are invalid
     */
    public Square getSquareAt(CoordinatePair coordinatePair) {
        return new Square(this.maze, coordinatePair);
    }

    /**
     * Returns a list of {@link CoordinatePair}s corresponding to the highlighted hint squares.
     *
     * @return a list of {@link CoordinatePair}s corresponding to the highlighted hint squares
     */
    public List<CoordinatePair> getHintCoordinateList() {
        int pathLength = this.difficultyLevel.getHintLength();
        List<Node<Content>> fullPath = this.maze.getShortestPath(
                this.maze.getNodeAt(this.playerPosition.down, this.playerPosition.across),
                this.maze.getEnd());
        List<CoordinatePair> truncatedPath = new LinkedList<>();
        for (int i = 0; i < pathLength; i++) {
            truncatedPath.add(i, this.maze.getCoordinatesOf(fullPath.get(i)));
        }
        return truncatedPath;
    }

    /**
     * Returns a {@link CoordinatePair} corresponding to the position of the player.
     *
     * @return a {@link CoordinatePair} corresponding to the position of the player
     */
    public CoordinatePair getPlayerPosition() {
        return this.playerPosition;
    }

    /**
     * Sets the position of the player according to the given coordinates.
     *
     * @param playerPosition a {@link CoordinatePair} corresponding to the new position of the player
     * @return true if the position of the player was successfully set to the given coordinates
     */
    public boolean setPlayerPosition(CoordinatePair playerPosition) {
        this.playerPosition = playerPosition;
        /* Handle player obtaining a credit */
        if (this.maze.getNodeAt(playerPosition.down, playerPosition.across).getValue().equals(Content.CREDIT)) {
            this.maze.getNodeAt(playerPosition.down, playerPosition.across).setValue(Content.EMPTY);
            this.numOfCoins++;
        }
        return true;
    }

    /**
     * Returns the number of coins that the player currently holds.
     *
     * @return the number of coins that the player currently holds
     */
    public int getNumberOfCoins() {
        return this.numOfCoins;
    }

    /**
     * Sets the number of coins that the player holds.
     *
     * @param numberOfCoins the new number of coins held by the player
     * @return true if the number of coins held by the player was successfully set to the given amount
     */
    public boolean setNumberOfCoins(int numberOfCoins) {
        this.numOfCoins = numberOfCoins;
        return true;
    }
}
