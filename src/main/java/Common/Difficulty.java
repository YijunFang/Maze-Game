package Common;

/**
 * An enum defining different constants for varying levels of difficulty.
 *
 * @author john
 */
public enum Difficulty {
    SIMPLE(5, 1, 5, 5),
    EASY(10, 2, 5, 4),
    MEDIUM(15, 3, 5, 3),
    HARD(20, 4, 5, 2);

    private int sideLength;
    private int numberOfCoins;
    private int hintDuration;
    private int hintLength;

    Difficulty(int sideLength, int numberOfCoins, int hintDuration, int hintLength) {
        this.sideLength = sideLength;
        this.numberOfCoins = numberOfCoins;
        this.hintDuration = hintDuration;
        this.hintLength = hintLength;
    }

    /**
     * Returns the side length of the maze for games with this difficulty.
     *
     * @return the side length of the maze for games with this difficulty
     */
    public int getSideLength() {
        return this.sideLength;
    }

    /**
     * Returns the number of coins present in the maze for games with this difficulty.
     *
     * @return the number of coins present in the maze for games with this difficulty
     */
    public int getNumberOfCoins() {
        return this.numberOfCoins;
    }

    /**
     * Returns the hint duration in seconds for games with this difficulty.
     *
     * @return the hint duration in seconds for games with this difficulty
     */
    public int getHintDuration() {
        return this.hintDuration;
    }

    /**
     * Returns the path length of the hint for games with this difficulty.
     *
     * @return the path length of the hint for games with this difficulty
     */
    public int getHintLength() {
        return this.hintLength;
    }
}
