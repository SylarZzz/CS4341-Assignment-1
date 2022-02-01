import java.util.ArrayList;

public enum Heuristic {
    ZERO((start, end) -> 0),

    MIN((start, end) -> {
        int horizontalDiff = Math.abs(start.getxPos() - end.getxPos());
        int verticalDiff = Math.abs(start.getyPos() - end.getyPos());
        return Math.min(verticalDiff, horizontalDiff);
    }),

    MAX((start, end) -> {
        int horizontalDiff = Math.abs(start.getxPos() - end.getxPos());
        int verticalDiff = Math.abs(start.getyPos() - end.getyPos());
        return Math.max(verticalDiff, horizontalDiff);
    }),

    SUM((start, end) -> {
        int horizontalDiff = Math.abs(start.getxPos() - end.getxPos());
        int verticalDiff = Math.abs(start.getyPos() - end.getyPos());
        return horizontalDiff + verticalDiff;
    }),

    ADMISSIBLE((start, end) -> {
        int horizontalDiff = Math.abs(start.getxPos() - end.getxPos());
        int verticalDiff = Math.abs(start.getyPos() - end.getyPos());

//===================================================================================
        // TODO So far this heuristic isn't giving the best paths sometimes (could be an A* bug though)
        /*
         * In order to be superior over the previous heuristic, could consider the potentially necessary cost to turn
         * on the current node in order to face the direction of the goal.
         */
        return horizontalDiff + verticalDiff + start.turnCost(end);
//===================================================================================

//===================================================================================
        // TODO So far this heuristic isn't giving the best paths sometimes (could be an A* bug though)
        /*
        In order to be superior over the previous heuristic, we could consider the terrain cost of nodes in between
        the current node (start) and the goal (end):
            1. Find the nearest nodes to the current node
            2. Filter out the nodes that are NOT moving in the direction towards the goal
            3. Filter out the goal itself as a possible neighbor
            4. Use the resulting neighbor's terrain score
         */
//        final ArrayList<Node> neighbors = start.getNeighbors();
//        final Node.Direction dirTowardsEnd =
//                Node.Direction.compute(start.getxPos(), start.getyPos(), end.getxPos(), end.getyPos());
//        neighbors.removeIf(neighbor -> !neighbor.isGoal() && neighbor.getDirection() == dirTowardsEnd);
//
//        int inBetweenTerrain = 0;
//        if(neighbors.size() > 0) {
//            inBetweenTerrain += neighbors.get(0).getTerrain();
//        }
//
//        return horizontalDiff + verticalDiff + inBetweenTerrain;
//===================================================================================

        /*
         * TODO this needs to be changed
         *      Technically this is inadmissible, it is overestimating costs of the goal node.
         *      If you are right next to the goal node, the difference of distances will sum to 1.
         *      However, since the terrain cost of the goal node is always 1 as well, this wil cause the
         *      heuristic to total to 2, which is inadmissible.
         */
//        return horizontalDiff + verticalDiff + end.getTerrain();
    }),

    INADMISSIBLE((start, end) ->
        ADMISSIBLE.compute(start, end) * 3);

    private final HeuristicFunc heuristicFunc;

    Heuristic(final HeuristicFunc heuristicFunc) {
        this.heuristicFunc = heuristicFunc;
    }

    public int compute(Node start, Node end) {
        return heuristicFunc.compute(start, end);
    }

    private interface HeuristicFunc {
        int compute(Node start, Node end);
    }
}
