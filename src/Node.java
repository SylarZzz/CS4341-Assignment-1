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
}
