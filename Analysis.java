
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Analysis {
    public static void main(String[] args) {
        // Analysis

        /*
            Issue: Currently path scores for heuristic 1 - 5 are not identical.
         */


        // Generate 10 boards and run each of them with the six heuristics
        // TODO remove limit of 1 board
        for (int k = 0; k < 1; k++) {
            System.out.println("Analysis of board number " + (k + 1) + ": ");

            // TODO change to size so that H1 takes 30 seconds (100x100 seems okay)
            Board b1 = BoardFactory.getBoard(100, 100);


            System.out.println("New board: ");
            System.out.println(b1);

            // Start & end node position in the new board
            final int[] startPos1 = b1.getStartPos();
            final int[] endPos1 = b1.getGoalPos();

            System.out.println("New board start pos: " + Arrays.toString(startPos1));
            System.out.println("New board end pos: " + Arrays.toString(endPos1));
            System.out.println();

            // Create start & end nodes
            final Node startNode1 = new Node(b1, startPos1[0], startPos1[1], Node.Direction.NORTH);
            final Node endNode1 = new Node(b1, endPos1[0], endPos1[1], null);

            // Run analysis with six heuristics
            for (int i = 1; i <= Heuristic.values().length; i++) {
                final Heuristic heuristicN = Heuristic.values()[i - 1];
                final AStarPathfinder aStarN = new AStarPathfinder(heuristicN);

                final Instant start = Instant.now();
                final ArrayList<Node> pathN = aStarN.createPath(startNode1, endNode1);
                final Instant stop = Instant.now();

                System.out.println("Heuristic " + i + ": ");
                System.out.println("Computation Time: " + Duration.between(start, stop).getSeconds() + " seconds");
                System.out.println("Path score: " + aStarN.getPathScore());
                System.out.println("Number of actions: " + aStarN.getNumActions());
                System.out.println("Number of nodes expanded: " + aStarN.getNumNodesExpanded());
                printPath(pathN);
                System.out.println();
            }
        }
    }

    private static void printPath(ArrayList<Node> pathNodes) {
        final char[][] origBoard = pathNodes.get(pathNodes.size() - 1).board.board;
        final char[][] board = new char[origBoard.length][origBoard[0].length];

        // copy old array into new one
        for(int i = 0; i < origBoard.length; i++) {
            System.arraycopy(origBoard[i], 0, board[i], 0, origBoard[0].length);
        }

        for(Node pathNode : pathNodes) {
            final char pathChar;
            if(pathNode.isGoal()) {
                pathChar = 'G';
            }
            else if(pathNode.getActions().contains(Node.Action.BASH)) {
                pathChar = 'B';
            }
            else {
                switch(pathNode.getDirection()) {
                    case NORTH -> pathChar = '^';
                    case EAST ->  pathChar = '>';
                    case SOUTH ->  pathChar = 'V';
                    case WEST ->  pathChar = '<';
                    default -> pathChar = ' ';
                }
            }
            board[pathNode.getyPos()][pathNode.getxPos()] = pathChar;
        }
        System.out.println(new Board(board));
    }
}

