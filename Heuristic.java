public enum Heuristic {
    ZERO((start, end) -> 0),
    MIN((start, end) -> {
        int horizontalDiff = start.getxPos() - end.getxPos();
        int verticalDiff = start.getyPos() - end.getyPos();
        return Math.min(verticalDiff, horizontalDiff);
    }),
    MAX((start, end) -> {
        int horizontalDiff = start.getxPos() - end.getxPos();
        int verticalDiff = start.getyPos() - end.getyPos();
        return Math.max(verticalDiff, horizontalDiff);
    }),
    SUM((start, end) -> {
        int horizontalDiff = start.getxPos() - end.getxPos();
        int verticalDiff = start.getyPos() - end.getyPos();
        return horizontalDiff + verticalDiff;
    }),
    ADMISSIBLE((start, end) -> {
        int horizontalDiff = start.getxPos() - end.getxPos();
        int verticalDiff = start.getyPos() - end.getyPos();
        return horizontalDiff + verticalDiff + start.getTerrain() + end.getTerrain();
    });


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
