import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
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
                    for (int k = 0; k < splitLine.length; k++) {
                        boardArr[i][j] = splitLine[k].charAt(0);
                    }
                }
            }
        }

        //System.out.println("Random board: ");
        //System.out.println(BoardFactory.getBoard(4, 5));


        Board board = new Board(boardArr);

        System.out.println("Current board: ");
        System.out.println(board);

        final int[] startPos = board.getStartPos();
        final int[] endPos = board.getGoalPos();

        final Node startNode = new Node(board, startPos[0], startPos[1], Node.Direction.NORTH);
        final Node endNode = new Node(board, endPos[0], endPos[1], null);

        // Generate the desired path with the selected A* heuristic
        final AStar aStar = new AStar(heuristic);
        final ArrayList<AStar.PathNode> path = aStar.createPath(startNode, endNode);

        System.out.println("Path score: " + aStar.getPathScore());
        System.out.println("Number of actions: " + aStar.getNumActions());
        System.out.println("Number of nodes expanded: " + aStar.getNumNodesExpanded());
        System.out.println("Series of actions: ");

        for (int i = 0; i < path.size(); i++) {
            ArrayList<AStar.PathNode.Action> actions = path.get(i).getActions();
            for (int j = 0; j < actions.size(); j++) {
                System.out.println(actions.get(j).name());
            }
        }

        // Print path
        ArrayList<Node> pathNodes = new ArrayList<Node>();
        for (int i = 0; i < path.size(); i++) {
            pathNodes.add(path.get(i).getBoardNode());
        }
        System.out.println("Path: " + Arrays.toString(pathNodes.toArray()));
        System.out.println();




    }

}
