public enum Heurisitc {
    ZERO((start, end) -> 0);

    private final HeuristicFunc heuristicFunc;

    Heurisitc(final HeuristicFunc heuristicFunc) {
        this.heuristicFunc = heuristicFunc;
    }

    public int compute(Node start, Node end) {
        return heuristicFunc.compute(start, end);
    }

    private interface HeuristicFunc {
        int compute(Node start, Node end);
    }
}
