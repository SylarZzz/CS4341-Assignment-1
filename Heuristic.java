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
        return horizontalDiff + verticalDiff + start.getTerrain() + end.getTerrain();
    }),
    NONADMISSIBLE((start, end) -> {
        int horizontalDiff = Math.abs(start.getxPos() - end.getxPos());
        int verticalDiff = Math.abs(start.getyPos() - end.getyPos());
        return (horizontalDiff + verticalDiff + start.getTerrain() + end.getTerrain()) * 3;
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
