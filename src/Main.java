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


        System.out.println("Current board: " + Arrays.deepToString(boardArr));

        Board board = new Board(boardArr);

        final int[] startPos = board.getStartPos();
        final int[] endPos = board.getGoalPos();

        final Node startNode = new Node(board, startPos[0], startPos[1], Node.Direction.NORTH);
        final Node endNode = new Node(board, endPos[0], endPos[1], null);

        // Generate the desired path with the selected A* heuristic
        final AStar aStar = new AStar(heuristic);
        final ArrayList<AStar.PathNode> path = aStar.createPath(startNode, endNode);


    }

}
