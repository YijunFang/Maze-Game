package MazeRep.MazeSearch;

/**
 * An interface for a generic graph search heuristic function.
 *
 * @author john
 */
public interface SearchHeuristic<T> {
    int calculate(T start, T goal);
}
