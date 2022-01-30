import java.awt.desktop.SystemEventListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        char[][] boardArr = new char[3][4];

        File file = new File(args[0]);
        Scanner sc = new Scanner(file);


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

        System.out.println("Starting node position: " + Arrays.toString(board.getStartPos()));
        System.out.println("Goal node position: " + Arrays.toString(board.getGoalPos()));

        Node n1 = new Node(board,0, 0);
        System.out.println("Node (0, 0)'s terrain: " + n1.getTerrain());
        System.out.println("Node (0, 0) is start node? " + n1.isStart());
        System.out.println("Node (0, 0) is end node? " + n1.isGoal());

        Node n2 = new Node(board,0, 1);
        System.out.println("Node (0, 1) is start node? " + n2.isStart());
        System.out.println("Node (0, 1) is goal node? " + n2.isGoal());
    }

}
