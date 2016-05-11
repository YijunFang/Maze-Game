package MazeRep.MazeSearch;

import Common.CoordinatePair;

import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of the {@link SearchState} interface for A* search on a {@link MazeRep.GraphMaze}.
 *
 * @author john
 */
public class GraphMazeSearchState implements SearchState<CoordinatePair> {

    private CoordinatePair value;
    private CoordinatePair goal;
    private SearchState<CoordinatePair> previous;
    private int cost;
    private Integer heuristicValue;
    private SearchHeuristic<CoordinatePair> heuristic;

    public GraphMazeSearchState(CoordinatePair coordinatePair, CoordinatePair goalCoordinatePair, int cost, SearchState<CoordinatePair> previous) {
        this.value = coordinatePair;
        this.goal = goalCoordinatePair;
        this.cost = cost;
        this.previous = previous;
        this.heuristicValue = null;
        this.heuristic = new ManhattanDistanceHeuristic();
    }

    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public int getHeuristicValue() {
        if (this.heuristicValue == null) {
            this.heuristicValue = this.heuristic.calculate(this.value, this.goal);
        }
        return this.heuristicValue;
    }

    @Override
    public CoordinatePair getValue() {
        return this.value;
    }

    @Override
    public SearchState<CoordinatePair> getPrevious() {
        return this.previous;
    }

    @Override
    public List<CoordinatePair> constructPath() {
        List<CoordinatePair> path;
        if (this.previous == null) {
            path = new LinkedList<>();
        } else {
            path = this.previous.constructPath();
        }
        path.add(this.value);
        return path;
    }

    @Override
    public int compareTo(SearchState<CoordinatePair> t) {
        if (t == null) {
            throw new IllegalArgumentException("Cannot compare SearchState with null");
        }
        if (t.getClass() != GraphMazeSearchState.class) {
            throw new IllegalArgumentException("Cannot compare SearchStates of differing implementations");
        }
        GraphMazeSearchState castedT = (GraphMazeSearchState)t;
        return ((this.getCost() + this.getHeuristicValue()) - (castedT.getCost() + castedT.getHeuristicValue()));
    }
}
