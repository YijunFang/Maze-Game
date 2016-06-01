package GameRep;

import Common.Content;
import Common.CoordinatePair;
import MazeRep.Maze;

/**
 * The representation of a single cell in the maze.
 *
 * @author john
 */
public class Square {

    private Boolean borders[];
    private Content content;
    private CoordinatePair coordinatePair;

    /**
     * Constructs a new Square to represent the square at given coordinate in the given maze.
     *
     * @param maze           the maze to obtain data from
     * @param coordinatePair the coordinate to reference in the maze
     */
    Square(Maze<Content> maze, CoordinatePair coordinatePair) {
        this.content = maze.getNodeAt(coordinatePair.down, coordinatePair.across).getValue();
        this.coordinatePair = coordinatePair;
        this.borders = new Boolean[4];
        /* Set border values */
        if (coordinatePair.down == 0) {
            this.borders[SquareSide.UP.toInt()] = true;
        } else {
            this.borders[SquareSide.UP.toInt()] = !maze.areAdjacent(
                    maze.getNodeAt(coordinatePair.down, coordinatePair.across),
                    maze.getNodeAt(coordinatePair.down - 1, coordinatePair.across)
            );
        }
        if (coordinatePair.down == maze.getHeight() - 1) {
            this.borders[SquareSide.DOWN.toInt()] = true;
        } else {
            this.borders[SquareSide.DOWN.toInt()] = !maze.areAdjacent(
                    maze.getNodeAt(coordinatePair.down, coordinatePair.across),
                    maze.getNodeAt(coordinatePair.down + 1, coordinatePair.across)
            );
        }
        if (coordinatePair.across == 0) {
            this.borders[SquareSide.LEFT.toInt()] = true;
        } else {
            this.borders[SquareSide.LEFT.toInt()] = !maze.areAdjacent(
                    maze.getNodeAt(coordinatePair.down, coordinatePair.across),
                    maze.getNodeAt(coordinatePair.down, coordinatePair.across - 1)
            );
        }
        if (coordinatePair.across == maze.getLength() - 1) {
            this.borders[SquareSide.RIGHT.toInt()] = true;
        } else {
            this.borders[SquareSide.RIGHT.toInt()] = !maze.areAdjacent(
                    maze.getNodeAt(coordinatePair.down, coordinatePair.across),
                    maze.getNodeAt(coordinatePair.down, coordinatePair.across + 1)
            );
        }
    }

    /**
     * Returns the corresponding {@link CoordinatePair} of this Square in the maze.
     *
     * @return the corresponding {@link CoordinatePair} of this Square in the maze
     */
    public CoordinatePair getCoordinatePair() {
        return this.coordinatePair;
    }

    /**
     * Returns true if the specified side of this Square is a wall.
     *
     * @param side a {@link SquareSide} specifying which side of the Square to check
     * @return true if the specified side of this Square is a wall
     */
    public boolean isBorderedOn(SquareSide side) {
        return this.borders[side.toInt()];
    }

    /**
     * Returns the {@link Content} value of this Square.
     *
     * @return the {@link Content} value of this Square
     */
    public Content getContent() {
        return this.content;
    }
}
