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

    private int xPos;   // board[y][x]
    private int yPos;
    private boolean isStart;
    private boolean isGoal;
    private int terrain;
    private Direction direction;

    public Node(Board board, int xPos, int yPos, Direction direction) {
        this.board = board;

        this.xPos = xPos;
        this.yPos = yPos;

        this.direction = direction;
        
        // Determine isStart value
        if (board.getStartPos()[0] == xPos && board.getStartPos()[1] == yPos) {
            isStart = true;
        } else {
            isGoal = false;
        }

        // Determine isGoal value
        if (board.getGoalPos()[0] == xPos && board.getGoalPos()[1] == yPos) {
            isGoal = true;
        } else {
            isGoal = false;
        }
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

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return xPos == node.xPos &&
                yPos == node.yPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos);
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
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[i].length; j++) {
                char terrainChar = '\0';
                if (i == xPos && j == yPos - 1) {
                    terrainChar = board.getBoard()[i][j];
                }
                else if (i == (xPos - 1) && j == yPos) {
                    terrainChar = board.getBoard()[i][j];
                }
                else if (i == xPos + 1 && j == yPos) {
                    terrainChar = board.getBoard()[i][j];
                }
                else if (i == xPos && j == yPos + 1) {
                    terrainChar = board.getBoard()[i][j];
                }
                else {
                    continue;
                }

                neighbors.add(new Node(board, i, j, Direction.compute(xPos, yPos, i, j)));
            }
        }

        return neighbors;
    }

    public double turnCost(Node neighbor) {
        if(neighbor.direction != this.direction) {
            return this.terrain / 2.0;
        }

        return 0;
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
