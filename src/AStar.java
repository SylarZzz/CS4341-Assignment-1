import java.util.*;

public class AStar {

    public static PathNode createPath(final Heurisitc heurisitc, Node from, Node to) {
        final Set<Node> alreadySeen = new HashSet<>();
        final Queue<PathNode> queue = new PriorityQueue<>();
        queue.add(new PathNode(null, from, heurisitc.compute(from, to), 0));

        // Continue looking through the path
        while (!queue.isEmpty()) {
            final PathNode currNode = queue.remove();
            final Node currBoardNode = currNode.boardNode;

            // The end node has been found
            if(to.equals(currNode.boardNode)) {
                return currNode;
            }

            // Get neighbor nodes
            ArrayList<Node> neighbors = currNode.boardNode.getNeighbors();

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
                            heurisitc.compute(neighbor, to),
                            boostActions));
                }

                // Provide the option for not boosting
                actions.add(PathNode.Action.FORWARD);

                // Add to the queue
                queue.add(new PathNode(
                        currNode,
                        neighbor,
                        score,
                        heurisitc.compute(neighbor, to),
                        actions));
            }

            alreadySeen.add(currNode.boardNode);
        }

        return null;
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

        private PathNode(PathNode prevNode, Node boardNode, double score, int heuristic, ArrayList<Action> actions) {
            this.prevNode = prevNode;
            this.boardNode = boardNode;
            this.score = score;
            this.heuristic = heuristic;
            this.actions = actions;
        }

        @Override
        public int compareTo(PathNode other) {
            // Output whether or not they are equal
            if (this.equals(other)) {
                return 0;
            }
            // Output whether or not the given is less than the current node
            else {
                return (int) Math.ceil(score + heuristic);
            }
        }
    }
}
