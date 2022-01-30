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
        queue.add(new PathNode(null, from, heuristic.compute(from, to), 0, new ArrayList<>()));

        pathScore = 0;
        numActions = 0;
        numNodesExpanded = 0;

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
                    pathScore += nextNode.score;

                    nextNode = nextNode.getPrevNode();
                }

                return path;
            }

            // Get neighbor nodes
            ArrayList<Node> neighbors = currNode.boardNode.getNeighbors();
            numNodesExpanded += neighbors.size();

            // Add the neighbors to the queue (if they haven't already been seen, or contain a piece)
            for(Node neighbor : neighbors) {
                // If the coordinate has already been seen, skip the coordinate
                if(alreadySeen.contains(neighbor)) {
                    continue;
                }

                final ArrayList<PathNode.Action> actions = new ArrayList<>();
                double score = 0; // neighbor.getTerrain() + currBoardNode.turnCost(neighbor)

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
                            boostActions));
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
            }

            alreadySeen.add(currNode.boardNode);
        }

        return new ArrayList();
    }

    public int getNumNodesExpanded() {
        return numNodesExpanded;
    }

    public double getPathScore() {
        return pathScore;
    }

    public int getNumActions() {
        return numActions;
    }

    public static class PathNode implements Comparable<PathNode> {
        enum Action {
            FORWARD,
            TURN,
            BOOST
        }

        private final PathNode prevNode;
        private final Node boardNode;
        private final double score;
        private final int heuristic;
        private final ArrayList<Action> actions;

        private final int cost;

        private PathNode(PathNode prevNode, Node boardNode, double score, int heuristic, ArrayList<Action> actions) {
            this.prevNode = prevNode;
            this.boardNode = boardNode;
            this.score = score;
            this.heuristic = heuristic;
            this.actions = actions;

            this.cost = (int) Math.ceil(score + heuristic);
        }

        @Override
        public int compareTo(PathNode other) {
            // Output whether or not they are equal
            if (this.equals(other)) {
                return 0;
            }
            // Output whether or not the given is less than the current node
            else {
                return this.cost > other.cost ? 1 : -1;
            }
        }

        public PathNode getPrevNode() {
            return prevNode;
        }
    }
}
