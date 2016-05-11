package GameRep;

import Common.Content;
import Common.CoordinatePair;

/**
 * The representation of a single cell in the maze.
 *
 * @author john
 */
public class Square {
    Square() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the corresponding {@link CoordinatePair} of this Square in the maze.
     *
     * @return the corresponding {@link CoordinatePair} of this Square in the maze
     */
    public CoordinatePair getCoordinatePair() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true if the specified side of this Square is a wall.
     *
     * @param side a {@link SquareSide} specifying which side of the Square to check
     * @return true if the specified side of this Square is a wall
     */
    public boolean isBorderedOn(SquareSide side) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the {@link Content} value of this Square.
     *
     * @return the {@link Content} value of this Square
     */
    public Content getContent() {
        throw new UnsupportedOperationException();
    }
}
