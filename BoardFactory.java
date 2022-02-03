import java.util.Random;

public class BoardFactory {

    private BoardFactory() {}

    public static Board getBoard(int totalRow, int totalCol) {
        final char[][] board = new char[totalRow][totalCol];
        // TODO REMOVE SEED
        final Random rand = new Random(1234567890);

        // Compute terrain values
        for(int row = 0; row < totalRow; row++) {
            for(int col = 0; col < totalCol; col++) {
                board[row][col] = String.valueOf(Math.abs(rand.nextInt() % 9) + 1).charAt(0);
            }
        }

        // Compute starting position
        final int startRow = Math.abs(rand.nextInt() % totalRow);
        final int startCol = Math.abs(rand.nextInt() % totalCol);
        board[startRow][startCol] = 'S';

        // Compute goal position
        final int goalRow = Math.abs(rand.nextInt() % totalRow);
        final int goalCol = Math.abs(rand.nextInt() % totalCol);
        board[goalRow][goalCol] = 'G';

        return new Board(board);
    }
}
