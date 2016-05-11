package GameRep;

import Common.CoordinatePair;
import Common.Difficulty;

import java.util.List;

/**
 * The representation of the maze game at the current time.
 *
 * @author john
 */
public class GameState {
    /**
     * Creates a new game with the given difficulty level.
     *
     * @param difficultyLevel the difficulty level of the game
     */
    public GameState(Difficulty difficultyLevel) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the difficult level of this game.
     *
     * @return the difficulty level of this game
     */
    public Difficulty getDifficultyLevel() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the {@link Square} at the given coordinates.
     *
     * @param coordinatePair the coordinates of the {@link Square} to return
     * @return the {@link Square} at the given coordinates
     * @throws IllegalArgumentException if the given coordinates are invalid
     */
    public Square getSquareAt(CoordinatePair coordinatePair) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a list of {@link CoordinatePair}s corresponding to the highlighted hint squares.
     *
     * @return a list of {@link CoordinatePair}s corresponding to the highlighted hint squares
     */
    public List<CoordinatePair> getHintCoordinateList() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a {@link CoordinatePair} corresponding to the position of the player.
     *
     * @return a {@link CoordinatePair} corresponding to the position of the player
     */
    public CoordinatePair getPlayerPosition() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the position of the player according to the given coordinates.
     *
     * @param playerPosition a {@link CoordinatePair} corresponding to the new position of the player
     * @return true if the position of the player was successfully set to the given coordinates
     */
    public boolean setPlayerPosition(CoordinatePair playerPosition) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the number of coins that the player currently holds.
     *
     * @return the number of coins that the player currently holds
     */
    public int getNumberOfCoins() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the number of coins that the player holds.
     *
     * @param numberOfCoins the new number of coins held by the player
     * @return true if the number of coins held by the player was successfully set to the given amount
     */
    public boolean setNumberOfCoins(int numberOfCoins) {
        throw new UnsupportedOperationException();
    }
}
