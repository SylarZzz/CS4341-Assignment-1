import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AStar {
    public static void main(String[] args) throws FileNotFoundException {
        char[][] boardArr = new char[3][4];

        // Get board file name
        File file = new File(args[0]);
        Scanner sc = new Scanner(file);

        // Get heuristic choice
        final Heuristic heuristic = Heuristic.values()[Integer.parseInt(args[1]) - 1];

        while(sc.hasNextLine()) {
            for (int i = 0; i < boardArr.length; i++) {
                String[] line = sc.nextLine().trim().split("\\t");
                for (int j = 0; j < line.length; j++) {
                    String[] splitLine = line[j].split("\\t");
                    for (String s : splitLine) {
                        boardArr[i][j] = s.charAt(0);
                    }
                }
            }
        }

        Board board = new Board(boardArr);

        System.out.println("Current board: ");
        System.out.println(board);

        final int[] startPos = board.getStartPos();
        final int[] endPos = board.getGoalPos();

        final Node startNode = new Node(board, startPos[0], startPos[1], Node.Direction.NORTH);
        final Node endNode = new Node(board, endPos[0], endPos[1], null);

        // Generate the desired path with the selected A* heuristic
        final AStarPathfinder aStar = new AStarPathfinder(heuristic);
        final ArrayList<Node> path = aStar.createPath(startNode, endNode);

        System.out.println("Path score: " + aStar.getPathScore());
        System.out.println("Number of actions: " + aStar.getNumActions());
        System.out.println("Number of nodes expanded: " + aStar.getNumNodesExpanded());
        System.out.println("Series of actions: ");

        for(Node pathNode : path) {
            ArrayList<Node.Action> actions = pathNode.getActions();
            for(Node.Action action : actions) {
                System.out.println(action.name());
            }
        }
    }

}
