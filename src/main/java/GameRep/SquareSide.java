package GameRep;

/**
 * An enum defining the 4 sides of a {@link Square}.
 *
 * @author john
 */
public enum SquareSide {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    private int val;

    SquareSide(int val) {
        this.val = val;
    }

    /**
     * Returns the integer representation of this enum.
     *
     * @return the integer representation of this enum
     */
    public int toInt() {
        return this.val;
    }
}
