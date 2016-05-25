package GameRep;

import Common.Content;
import Common.CoordinatePair;
import Common.Difficulty;
import MazeRep.GraphMaze;
import MazeRep.Maze;
import MazeRep.MazeFactory;
import MazeRep.Node;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * The representation of the maze game at the current time.
 *
 * @author john
 */
public class GameState {
    //TODO Implement textures instead of lines
    //      
    private Maze<Content> maze;
    private Difficulty difficultyLevel;
    private CoordinatePair playerPosition;
    private CoordinatePair goalPosition;
    private int numOfCoins;

    private GameState() {
        // dummy constructor for clone() method
    }

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
        this.goalPosition = this.maze.getCoordinatesOf(this.maze.getEnd());
        this.difficultyLevel = difficultyLevel;
        /* Place coins in maze */
        Random random = new Random();
        for (this.numOfCoins = 0; this.numOfCoins < difficultyLevel.getNumberOfCoins(); ) {
            CoordinatePair randomCoordinatePair = new CoordinatePair(
                    random.nextInt(this.maze.getHeight()),
                    random.nextInt(this.maze.getLength()));
            if (!randomCoordinatePair.equals(this.playerPosition)) {
                Node<Content> randomNode = this.maze.getNodeAt(randomCoordinatePair.down, randomCoordinatePair.across);
                if (randomNode.getValue().equals(Content.EMPTY)) {
                    randomNode.setValue(Content.CREDIT);
                    this.numOfCoins++;
                }
            }
        }
        this.numOfCoins = 0;
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
     * @throws IndexOutOfBoundsException if the given coordinates are invalid
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
        for (int i = 0; i < pathLength && i < fullPath.size(); i++) {
            truncatedPath.add(i, this.maze.getCoordinatesOf(fullPath.get(i)));
            System.out.println(truncatedPath.get(i).across + ", " + truncatedPath.get(i).down);
        }
        return truncatedPath;
    }

    /**
     * Returns a {@link CoordinatePair} corresponding to the goal square.
     *
     * @return a {@link CoordinatePair} corresponding to the goal square
     */
    public CoordinatePair getGoalPosition() {
        return this.goalPosition;
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
     * @throws IndexOutOfBoundsException if the given coordinates are invalid
     */
    public boolean setPlayerPosition(CoordinatePair playerPosition) {
        /* Call maze.getNodeAt() to indirectly check for valid coordinates */
        this.maze.getNodeAt(playerPosition.down, playerPosition.across);
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
     * Sets the number of coins that the player holds to a new positive integer value.
     *
     * @param numberOfCoins the new number of coins held by the player
     * @return true if the number of coins held by the player was successfully set to the given amount
     * @throws IllegalArgumentException if the new value for the number of coins held is invalid
     */
    public boolean setNumberOfCoins(int numberOfCoins) {
        if (numberOfCoins < 0) {
            throw new IllegalArgumentException("Number of coins held by the player cannot be negative.");
        }
        if (numberOfCoins > this.difficultyLevel.getNumberOfCoins()) {
            throw new IllegalArgumentException("Number of coins held by the player cannot exceed the maximum number of coins for this difficulty level.");
        }
        this.numOfCoins = numberOfCoins;
        return true;
    }

    /**
     * Returns a deep copy of this GameState
     *
     * @return a deep copy of this GameState
     */
    public GameState clone() {
        GameState copy = new GameState();
        copy.maze = this.maze.clone();
        copy.difficultyLevel = this.difficultyLevel;
        copy.numOfCoins = this.numOfCoins;
        copy.playerPosition = new CoordinatePair(
                this.playerPosition.down,
                this.playerPosition.across);
        copy.goalPosition = new CoordinatePair(
                this.goalPosition.down,
                this.goalPosition.across);
        return copy;
    }

    public static void save(GameState gs, FileOutputStream ofs) {
        try {
            ObjectOutputStream oofs = new ObjectOutputStream(ofs);
            oofs.writeObject(gs);
            oofs.flush();
            oofs.close();
        } catch (Exception e) {
            throw new RuntimeException("Bad file output stream");
        }
    }

    public static GameState load(FileInputStream ifs) {
        try {
            ObjectInputStream oifs = new ObjectInputStream(ifs);
            GameState gs = (GameState)oifs.readObject();
            oifs.close();
            return gs;
        } catch (Exception e) {
            throw new RuntimeException("Bad file input stream");
        }
    }
}
