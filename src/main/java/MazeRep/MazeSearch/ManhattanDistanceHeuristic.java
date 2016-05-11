package MazeRep.MazeSearch;

import Common.CoordinatePair;

/**
 * An implementation of the {@link SearchHeuristic} interface for a maze using the Total Manhattan Distance formula.
 *
 * @author john
 */
public class ManhattanDistanceHeuristic implements SearchHeuristic<CoordinatePair> {
    @Override
    public int calculate(CoordinatePair start, CoordinatePair goal) {
        if (start == null) {
            throw new IllegalArgumentException("start must not be null");
        }
        if (goal == null) {
            throw new IllegalArgumentException("goal must not be null");
        }
        return Math.abs(start.down - goal.down) + Math.abs(start.across - goal.across);
    }
}
