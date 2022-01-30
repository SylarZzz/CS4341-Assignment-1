import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

public class Board {

    public int rows;
    public int cols;
    public char[][] board;



    public Board(char[][] board) {
        rows = 3;
        cols = 4;
        this.board = board;
    }

/*
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()) {
            for (int i = 0; i < board.length; i++) {
                String[] line = sc.nextLine().trim().split(" ");
                for (int j = 0; j < line.length; j++) {
                    board[i][j] = line[j];
                }
            }
        }
        System.out.println("Current board: " + Arrays.deepToString(board));
        System.out.println("Starting node ");
    }

 */


    public int getTerrainVal (int row, int col) {
        int terrainVal = 0;

        if (board[row][col] != 'S' && board[row][col] != 'G') {
            terrainVal = Character.getNumericValue(board[row][col]);
        } else {
            System.out.println("Error: You are either trying to get the terrain value of the start or the goal node.");
            return -1;
        }

        return terrainVal;
    }

    public int[] getStartPos() {
        int[] pos = new int[2];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'S') {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }

        return pos;
    }

    public int[] getGoalPos() {
        int[] pos = new int[2];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'G') {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }

        return pos;
    }

    public char[][] getBoard() {
        return board;
    }
}
