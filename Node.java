
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
}
