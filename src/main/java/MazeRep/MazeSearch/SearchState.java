package MazeRep.MazeSearch;

import MazeRep.Node;

import java.util.List;

/**
 * An interface for a generic search state used for A* graph search.
 *
 * @author john
 */
public interface SearchState<T> extends Comparable<SearchState<T>> {
    /**
     * Returns the total cost thus far to reach this SearchState.
     *
     * @return the total cost thus far to reach this SearchState
     */
    int getCost();

    /**
     * Returns the heuristic value from this SearchState to the goal.
     *
     * @return a heuristic value from this SearchState to the goal
     */
    int getHeuristicValue();

    /**
     * Returns the current value of this SearchState.
     *
     * @return the current value of this SearchState
     */
    T getValue();

    /**
     * Returns the SearchState directly before this SearchState.
     *
     * @return the SearchState directly before this SearchState
     */
    SearchState<T> getPrevious();

    /**
     * Recursively constructs a path from the beginning SearchState to this SearchState.
     *
     * @return a List of values from the beginning SearchState to this SearchState
     */
    List<T> constructPath();
}
