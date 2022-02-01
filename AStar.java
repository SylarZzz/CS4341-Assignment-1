import java.util.*;

public class AStar {

    private final Heuristic heuristic;

    private double pathScore = 0;
    private int numActions = 0;
    private int numNodesExpanded = 0;

    public AStar(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public ArrayList<PathNode> createPath(Node from, Node to) {
        final Set<Node> alreadySeen = new HashSet<>();
        final Queue<PathNode> queue = new PriorityQueue<>();
        queue.add(new PathNode(null, from, 0, heuristic.compute(from, to), new ArrayList<>(), 0));

        numActions = 0;

        // Continue looking through the path
        while (!queue.isEmpty()) {
            final PathNode currNode = queue.remove();
            final Node currBoardNode = currNode.boardNode;

            // The end node has been found
            if(to.equals(currNode.boardNode)) {
                ArrayList<PathNode> path = new ArrayList<>();
                PathNode nextNode = currNode;
                while(nextNode != null) {
                    path.add(0, nextNode);

                    numActions += nextNode.actions.size();

                    nextNode = nextNode.getPrevNode();
                }

                numNodesExpanded = currNode.numNodesExpanded;
                pathScore = currNode.cost;
                pathScore = 100 - pathScore;

                return path;
            }

            // Get neighbor nodes
            ArrayList<Node> neighbors = currNode.boardNode.getNeighbors();
            Set<Node> notSeenNeighbors = new HashSet<>(neighbors);
            notSeenNeighbors.removeAll(alreadySeen);
            final int numNodesExpanded = notSeenNeighbors.size();

            // Add the neighbors to the queue (if they haven't already been seen, or contain a piece)
            for(Node neighbor : neighbors) {
                // If the coordinate has already been seen, skip the coordinate
                if(alreadySeen.contains(neighbor)) {
                    continue;
                }

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

                if(!currNode.actions.contains(PathNode.Action.BOOST) && neighbor.getTerrain() >= 3 &&
                        (movingFarFromX || movingFarFromY)) {
                    // Add a boost action node as an option
                    final ArrayList<PathNode.Action> boostActions = new ArrayList<>(actions);
                    boostActions.add(PathNode.Action.BOOST);
                    queue.add(new PathNode(
                            currNode,
                            neighbor,
                            score + 3,
                            heuristic.compute(neighbor, to),
                            boostActions,
                            numNodesExpanded));
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
                        actions,
                        numNodesExpanded));
            }

            alreadySeen.add(currNode.boardNode);
        }

        return new ArrayList<PathNode>();
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
        private final int score;
        private final int heuristic;
        private final ArrayList<Action> actions;

        private int cost;

        private final int numNodesExpanded;

        private PathNode(PathNode prevNode, Node boardNode, int score, int heuristic, ArrayList<Action> actions,
                         int numNodesExpanded) {
            this.prevNode = prevNode;
            this.boardNode = boardNode;
            this.score = score;
            this.heuristic = heuristic;
            this.actions = actions;

            this.cost = score;
            if(prevNode != null) {
                this.cost += prevNode.score;
            }

            this.numNodesExpanded = numNodesExpanded + (prevNode != null ? prevNode.numNodesExpanded : 0);
        }

        @Override
        public int compareTo(PathNode other) {
            // Output whether or not they are equal
            if (this.equals(other)) {
                return 0;
            }
            // Output whether or not the given is less than the current node
            else {
                return this.cost + heuristic > other.cost + heuristic ? 1 : -1;
            }
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
    }
}
