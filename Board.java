
public class Board {

    public int rows;
    public int cols;
    public char[][] board;

    public Board(char[][] board) {
        rows = board.length;
        cols = board[0].length;
        this.board = board;
    }

    public int getTerrainVal (int row, int col) {
        int terrainVal;

        if (board[row][col] != 'S' && board[row][col] != 'G') {
            terrainVal = Character.getNumericValue(board[row][col]);
        } else {
            return 1;
        }

        return terrainVal;
    }

    public int[] getStartPos() {
        int[] pos = new int[2];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'S') {
                    pos[0] = j;
                    pos[1] = i;
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
                    pos[0] = j;
                    pos[1] = i;
                }
            }
        }

        return pos;
    }

    public char[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder();
        for(char[] row : board) {
            for(char value : row) {
                boardStr.append(value);
            }
            boardStr.append('\n');
        }

        return boardStr.toString();
    }
}
