import java.util.ArrayList;
import java.util.Arrays;

public class Analysis {
    public static void main(String[] args) {
        // Analysis

        /*
            Issue: Currently path scores for heuristic 1 - 5 are not identical.
         */

        // Generate boards
        System.out.println("Analysis for the random board generated: ");

        Board b1 = BoardFactory.getBoard(10, 10);

        System.out.println("New board: ");
        System.out.println(b1);

        // Start & end node position in the new board
        final int[] startPos1 = b1.getStartPos();
        final int[] endPos1 = b1.getGoalPos();

        System.out.println("New board start pos: " + Arrays.toString(startPos1));
        System.out.println("New board end pos: " + Arrays.toString(endPos1));


        // Create start & end nodes
        final Node startNode1 = new Node(b1, startPos1[0], startPos1[1], Node.Direction.NORTH);
        final Node endNode1 = new Node(b1, endPos1[0], endPos1[1], null);

        for (int i = 1; i < 7; i++) {
            final Heuristic heuristicN = Heuristic.values()[i - 1];
            final AStar aStarN = new AStar(heuristicN);
            final ArrayList<AStar.PathNode> pathN = aStarN.createPath(startNode1, endNode1);
            System.out.println("Heuristic " + i + ": ");
            System.out.println("Path score: " + aStarN.getPathScore());
            System.out.println("Number of actions: " + aStarN.getNumActions());
            System.out.println("Number of nodes expanded: " + aStarN.getNumNodesExpanded());
            System.out.println();
        }
    }
}
