import java.util.*;

public class AStar {

    private final Heuristic heuristic;

    private double pathScore = 0;
    private int numActions = 0;
    private int numNodesExpanded = 0;

    public AStar(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    /* TODO Heuristic 1-5 isn't always identical
     * This isn't just heuristic #5 either.
     * Sometimes #1-2 are doing better in terms of score compared to the others.
     * Sometimes #5 is doing better than the rest as well (which does't make sense).
     * This seems to prove it is something wrong with A* still.
     */
    public ArrayList<PathNode> createPath(Node from, Node to) {
        final Set<PathNode> alreadySeen = new HashSet<>();
        final Queue<PathNode> queue = new PriorityQueue<>();
        queue.add(new PathNode(null, from, 0, heuristic.compute(from, to), new ArrayList<>()));

        numActions = 0;
        numNodesExpanded = 0;
        pathScore = 0;

        // Continue looking through the path
        while (!queue.isEmpty()) {
            final PathNode currNode = queue.remove();
            final Node currBoardNode = currNode.boardNode;

            // If the coordinate has already been seen, skip the coordinate
            if(alreadySeen.contains(currNode)) {
                numNodesExpanded--;
                continue;
            }

            // The end node has been found
            if(to.equals(currNode.boardNode)) {
                ArrayList<PathNode> path = new ArrayList<>();
                PathNode nextNode = currNode;
                while(nextNode != null) {
                    path.add(0, nextNode);

                    numActions += nextNode.actions.size();

                    nextNode = nextNode.getPrevNode();
                }

                pathScore = 100 - currNode.totalCost;

                return path;
            }

            // Get neighbor nodes
            ArrayList<Node> neighbors = currNode.boardNode.getNeighbors();

            // Add the neighbors to the queue (if they haven't already been seen, or contain a piece)
            for(Node neighbor : neighbors) {
                final ArrayList<PathNode.Action> actions = new ArrayList<>();
                int score = 0;

                // If the current node is not facing the same direction as the neighbor, a turn action is required
                if(neighbor.getDirection() != currBoardNode.getDirection()) {
                    // If a boost action was previously used on currNode, then only forward can be used, no turns
                    if(currNode.actions.contains(PathNode.Action.BOOST)) {
                        continue;
                    }
                    actions.add(PathNode.Action.TURN);
                    score += currBoardNode.turnCost(neighbor);
                }

                /*
                Determine if boost should be done on this neighbor:
                1. Current node did not just boost
                2. Neighbor has at least 3 terrain value
                3. Is current neighbor at least 2 straight spaces away from the board border?
                4. If yes, set the current node to use a boost action
                 */
                boolean farXBorder = neighbor.getxPos() >= 1 && neighbor.getxPos() <= neighbor.board.cols - 2;
                boolean movingFarFromX = farXBorder &&
                        (neighbor.getDirection().equals(Node.Direction.WEST) ||
                            neighbor.getDirection().equals(Node.Direction.EAST));

                boolean farYBorder = neighbor.getyPos() >= 1 && neighbor.getyPos() <= neighbor.board.rows - 2;
                boolean movingFarFromY = farYBorder &&
                        (neighbor.getDirection().equals(Node.Direction.NORTH) ||
                            neighbor.getDirection().equals(Node.Direction.SOUTH));

                if(!currNode.actions.contains(PathNode.Action.BOOST) && neighbor.getTerrain() > 3 &&
                        (movingFarFromX || movingFarFromY)) {
                    // Add a boost action node as an option
                    final ArrayList<PathNode.Action> boostActions = new ArrayList<>(actions);
                    boostActions.add(PathNode.Action.BOOST);
                    queue.add(new PathNode(
                            currNode,
                            neighbor,
                            score + 3,
                            heuristic.compute(neighbor, to),
                            boostActions));

                    // Increase expanded node count
                    numNodesExpanded += 1;
                }

                // Provide the option for not boosting
                actions.add(PathNode.Action.FORWARD);
                score += neighbor.getTerrain();

                // Add to the queue
                queue.add(new PathNode(
                        currNode,
                        neighbor,
                        score,
                        heuristic.compute(neighbor, to),
                        actions));
                // Increase expanded node count
                numNodesExpanded += 1;
            }

            alreadySeen.add(currNode);
        }

        return new ArrayList<>();
    }

    public double getPathScore() {
        return pathScore;
    }

    public int getNumActions() {
        return numActions;
    }

    public int getNumNodesExpanded() {
        return numNodesExpanded;
    }

    public static class PathNode implements Comparable<PathNode> {
        enum Action {
            FORWARD,
            TURN,
            BOOST
        }

        private final PathNode prevNode;
        private final Node boardNode;
        private final ArrayList<Action> actions;

        private final int totalCost;
        private final int futureCost;

        private PathNode(PathNode prevNode, Node boardNode, int score, int heuristic, ArrayList<Action> actions) {
            this.prevNode = prevNode;
            this.boardNode = boardNode;
            this.actions = actions;

            if(prevNode == null) {
                this.totalCost = 0;
            }
            else {
                this.totalCost = score + prevNode.totalCost;
            }

            this.futureCost = totalCost + heuristic;
        }

        public PathNode getPrevNode() {
            return prevNode;
        }

        public Node getBoardNode() {
            return boardNode;
        }

        public ArrayList<Action> getActions() {
            return actions;
        }

        @Override
        public int compareTo(PathNode other) {
            // Output whether or not they are equal
            if (this.equals(other)) {
                return 0;
            }
            // Output whether or not the given is less than the current node
            else {
                return this.futureCost > other.futureCost ? 1 : -1;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathNode pathNode = (PathNode) o;
            return Objects.equals(prevNode, pathNode.prevNode) &&
                    boardNode.equals(pathNode.boardNode) &&
                    actions.equals(pathNode.actions);
        }

        @Override
        public int hashCode() {
            return Objects.hash(prevNode, boardNode, actions);
        }
    }
}
