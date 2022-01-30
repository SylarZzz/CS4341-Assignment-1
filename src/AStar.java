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
            ArrayList<Node> neighbors = currNode.getNeighbors();

            // Add the neighbors to the queue (if they haven't already been seen, or contain a piece)
            for(Node neighbor : neighbors) {
                // If the coordinate has already been seen, skip the coordinate
                if(alreadySeen.contains(neighbor)) {
                    continue;
                }

                // Add to the queue
                queue.add(new PathNode(
                        currNode,
                        neighbor,
                        heurisitc.compute(neighbor, to),
                        neighbor.getTerrain() + currNode.distTravelled));
            }

            alreadySeen.add(currNode.boardNode);
        }

        return null;
    }

    public static class PathNode implements Comparable<PathNode> {
        private final PathNode prevNode;
        private final Node boardNode;
        private final int distTravelled;
        private final int distance;

        private PathNode(PathNode prevNode, Node boardNode, int distance, int distTravelled) {
            this.prevNode = prevNode;
            this.boardNode = boardNode;
            this.distance = distance;
            this.distTravelled = distTravelled;
        }

        @Override
        public int compareTo(PathNode other) {
            // Output whether or not they are equal
            if (this.equals(other)) {
                return 0;
            }
            // Output whether or not the given is less than the current node
            else {
                return this.distance > other.distance ? 1 : -1;
            }
        }

        public ArrayList<Node> getNeighbors() {
            ArrayList<Node> neighbors = new ArrayList<>();
            Board board = this.boardNode.board;

            return neighbors;
        }
    }
}
