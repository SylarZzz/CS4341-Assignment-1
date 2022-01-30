import java.util.Objects;

public class Node {

    Board board;

    private int xPos;   // board[x][y]
    private int yPos;
    private boolean isStart;
    private boolean isGoal;
    private int terrain;


    public Node(Board board, int xPos, int yPos) {
        this.board = board;

        this.xPos = xPos;
        this.yPos = yPos;

    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public boolean isStart() {
        // Check whether this node is start
        if (board.getStartPos()[0] == xPos && board.getStartPos()[1] == yPos) {
            isStart = true;
        } else {
            isGoal = false;
        }
        return isStart;
    }

    public boolean isGoal() {
        // Check whether this node is goal
        if (board.getGoalPos()[0] == xPos && board.getGoalPos()[1] == yPos) {
            isGoal = true;
        } else {
            isGoal = false;
        }
        return isGoal;
    }

    public int getTerrain() {
        terrain = board.getTerrainVal(xPos, yPos);
        return terrain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return xPos == node.xPos &&
                yPos == node.yPos &&
                isStart == node.isStart &&
                isGoal == node.isGoal &&
                terrain == node.terrain &&
                Objects.equals(board, node.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, xPos, yPos, isStart, isGoal, terrain);
    }

    // gets neighbor terrain value.
    /*
        a   b   c
        d   e   f
        g   h   i

        e's neighbors:
        char[] neighbor = [a, d, g, b, h, c, f, i]
     */
    public char[] getNeighbors() {
        char[] neighbor = new char[8];
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                if (i == xPos - 1 && j == yPos - 1) {
                    neighbor[0] = board.getBoard()[i][j];
                }
                else if (i == xPos && j == yPos - 1) {
                    neighbor[1] = board.getBoard()[i][j];
                }
                else if (i == xPos + 1 && j == yPos - 1) {
                    neighbor[2] = board.getBoard()[i][j];
                }
                else if (i == (xPos - 1) && j == yPos) {
                    neighbor[3] = board.getBoard()[i][j];
                }
                else if (i == xPos + 1 && j == yPos) {
                    neighbor[4] = board.getBoard()[i][j];
                }
                else if (i == xPos - 1 && j == yPos + 1) {
                    neighbor[5] = board.getBoard()[i][j];
                }
                else if (i == xPos && j == yPos + 1) {
                    neighbor[6] = board.getBoard()[i][j];
                }
                else if (i == xPos + 1 && j == yPos + 1) {
                    neighbor[7] = board.getBoard()[i][j];
                }
            }
        }

        return neighbor;
    }
}
