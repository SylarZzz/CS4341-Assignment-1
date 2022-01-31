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





        // Analysis

        /*
            Issue: Currently path scores for heuristic 1 - 5 are not identical.
         */
        
        // Generate boards
        System.out.println("Analysis for the random board generated: ");

        Board b1 = BoardFactory.getBoard(100, 100);

        //System.out.println("New board: ");
        //System.out.println(b1);

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



        /*
        System.out.println("Series of actions: ");
        // Print actions
        for (int i = 0; i < path1.size(); i++) {
            ArrayList<AStar.PathNode.Action> actions = path1.get(i).getActions();
            for (int j = 0; j < actions.size(); j++) {
                System.out.println(actions.get(j).name());
            }
        }

         */

        // Print path
        /*
        ArrayList<Node> pathNodes1 = new ArrayList<Node>();
        for (int i = 0; i < path1.size(); i++) {
            pathNodes1.add(path1.get(i).getBoardNode());
        }
        System.out.println("Path: " + Arrays.toString(pathNodes1.toArray()));

         */



    }

}
