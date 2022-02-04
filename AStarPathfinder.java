import java.math.BigInteger;
import java.time.Instant;
import java.util.*;

public class AStarPathfinder {

    private final Heuristic heuristic;

    private double pathScore = 0;
    private int numActions = 0;
    private int numNodesExpanded = 0;

    public AStarPathfinder(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    /* TODO Heuristic 1-5 isn't always identical
     * This isn't just heuristic #5 either.
     * Sometimes #1-2 are doing better in terms of score compared to the others.
     * Sometimes #5 is doing better than the rest as well (which does't make sense).
     * This seems to prove it is something wrong with A* still.
     */
    public ArrayList<Node> createPath(final Node startNode, final Node endNode) {
//        System.gc();
//        final BigInteger beforeUsedMem =
//                BigInteger.valueOf(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
        final Set<Node> alreadySeen = new HashSet<>();
        final Queue<Node> queue = new PriorityQueue<>();

        queue.add(startNode);
        alreadySeen.add(startNode);

        numActions = 0;
        numNodesExpanded = 0;
        pathScore = 0;

        // Continue looking through the path
        while (!queue.isEmpty()) {
            final Node currNode = queue.remove();

            // The end node has been found
            if(currNode.getxPos() == endNode.getxPos() && currNode.getyPos() == endNode.getyPos()) {
//                System.gc();
//                final BigInteger afterUsedMem =
//                        BigInteger.valueOf(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
//
//                System.out.println("Memory Usage: " + (afterUsedMem.subtract(beforeUsedMem)) + " bytes");

                ArrayList<Node> path = new ArrayList<>();
                Node nextNode = currNode;
                while(nextNode != null) {
                    path.add(0, nextNode);

                    numActions += nextNode.getActions().size();

                    nextNode = nextNode.getPrevNode();
                }

                pathScore = 100 - currNode.getTotalCost();

                return path;
            }

            // Get neighbor nodes
            ArrayList<Node> neighbors = currNode.getNeighbors(endNode, heuristic);

            // TODO node expanded count is inaccurate due to it be calculated without the nodes "technically" being expanded
            // Add the neighbors to the queue (if they haven't already been seen, or contain a piece)
            for(Node neighbor : neighbors) {
                // If the node has already been seen, skip the coordinate
                if(alreadySeen.contains(neighbor) || neighbor.isPathDuplicate()) {
                    continue;
                }

                // Add to the queue
                queue.add(neighbor);
                // Increase expanded node count
                numNodesExpanded += 1;
                alreadySeen.add(neighbor);
            }
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

}
