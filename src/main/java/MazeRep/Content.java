package MazeRep;

/**
 * Values representing the content in an individual square in the maze.
 *
 * @author john
 */
public enum Content {
    /**
     * No content
     */
    EMPTY(0),
    /**
     * A hint credit
     */
    CREDIT(1);

    private int val;

    Content(int val) {
        this.val = val;
    }

    /**
     * Returns an integer representation of this Content value.
     *
     * @return an integer representation of this Content value
     */
    public int toInt() {
        return this.val;
    }
}
