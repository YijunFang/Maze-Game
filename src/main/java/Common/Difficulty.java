package Common;

/**
 * An enum defining different constants for varying levels of difficulty.
 *
 * @author john
 */
public enum Difficulty {
    EASY(5, 1, 5, 5),
    MEDIUM(10, 2, 5, 4),
    HARD(15, 3, 5, 3);

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
