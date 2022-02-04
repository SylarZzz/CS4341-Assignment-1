import java.util.ArrayList;
import java.util.Objects;

public class Node {

    enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST;

        public static Direction compute(int xPosFrom, int yPosFrom, int xPosTo, int yPosTo) {
            int xDiff = xPosTo - xPosFrom, yDiff = yPosTo - yPosFrom;

            if(xDiff == 0 && yDiff < 0) {
                return NORTH;
            }
            else if(xDiff == 0 && yDiff > 0) {
                return SOUTH;
            }
            else if(xDiff < 0 && yDiff == 0) {
                return WEST;
            }
            else {
                return EAST;
            }
        }
    }

    Board board;

    private final int xPos;   // board[y][x]
    private final int yPos;
    private boolean isStart;
    private boolean isGoal;
    private final int terrain;
    private final Direction direction;

    public Node(Board board, int xPos, int yPos, Direction direction) {
        this.board = board;

        this.xPos = xPos;
        this.yPos = yPos;

        this.terrain = board.getTerrainVal(yPos, xPos);

        this.direction = direction;
        
        // Determine isStart value
        isStart = board.getStartPos()[0] == xPos && board.getStartPos()[1] == yPos;

        // Determine isGoal value
        isGoal = board.getGoalPos()[0] == xPos && board.getGoalPos()[1] == yPos;
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
        isGoal = board.getGoalPos()[0] == xPos && board.getGoalPos()[1] == yPos;
        return isGoal;
    }

    public int getTerrain() {
        return terrain;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return xPos == node.xPos &&
            yPos == node.yPos &&
            terrain == node.terrain;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos, terrain);
    }

    // gets neighbor terrain value.
    /*
        a   b   c
        d   e   f
        g   h   i

        e's neighbors:
        char[] neighbor = [a, d, g, b, h, c, f, i]
     */
    public ArrayList<Node> getNeighbors() {
        final ArrayList<Node> neighbors = new ArrayList<>();

        if(xPos > 0) {
            neighbors.add(new Node(board, xPos - 1, yPos, Direction.compute(xPos, yPos, xPos - 1, yPos)));
        }

        if(xPos < board.cols - 1) {
            neighbors.add(new Node(board, xPos + 1, yPos, Direction.compute(xPos, yPos, xPos + 1, yPos)));
        }

        if(yPos > 0) {
            neighbors.add(new Node(board, xPos, yPos - 1, Direction.compute(xPos, yPos, xPos, yPos - 1)));
        }

        if(yPos < board.rows - 1) {
            neighbors.add(new Node(board, xPos, yPos + 1, Direction.compute(xPos, yPos, xPos, yPos + 1)));
        }

        return neighbors;
    }

    public int turnCost(final Direction newDirection) {
        double turnCost = 0;
        if(newDirection != this.direction) {
            turnCost = this.terrain / 2.0;
        }

        final int ordDiff = Math.abs(newDirection.ordinal() - direction.ordinal());
        if(ordDiff == 2) {
            turnCost *= 2;
        }

        return (int) Math.ceil(turnCost);
    }

    @Override
    public String toString() {
        return "Node{" +
                "board=" + board +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                ", isStart=" + isStart +
                ", isGoal=" + isGoal +
                ", terrain=" + terrain +
                ", direction=" + direction +
                '}';
    }
}
