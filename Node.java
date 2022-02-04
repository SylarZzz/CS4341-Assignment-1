import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class Node implements Comparable<Node> {

    enum Action {
        FORWARD,
        LEFT,
        RIGHT,
        BASH;

        public static ArrayList<Action> computeTurnDir(final Direction startDir, final Direction endDir) {
            final ArrayList<Action> turnActions = new ArrayList<>();
            int ordDiff = endDir.ordinal() - startDir.ordinal();

            // 180 degree turns
            if(Math.abs(ordDiff) == 2) {
                turnActions.add(LEFT);
                turnActions.add(LEFT);
            }
            // 90 degree turns
            else {
                if(ordDiff >= 3) {
                    ordDiff = 1;
                }
                else if(ordDiff <= -3) {
                    ordDiff = -1;
                }

                if(ordDiff == 1) {
                    turnActions.add(RIGHT);
                }
                else if(ordDiff == -1) {
                    turnActions.add(LEFT);
                }
            }

            return turnActions;
        }
    }

    enum Direction {
        NORTH,
        EAST,
        SOUTH,
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

    public Node getPrevNode() {
        return prevNode;
    }

    private Node prevNode;

    public ArrayList<Action> getActions() {
        return actions;
    }

    private ArrayList<Action> actions;
    private int score;

    public int getTotalCost() {
        return totalCost;
    }

    private int totalCost;
    private int futureCost;

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

        actions = new ArrayList<>();
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

    // gets neighbor terrain value.
    /*
        a   b   c
        d   e   f
        g   h   i

        e's neighbors:
        char[] neighbor = [a, d, g, b, h, c, f, i]
     */
    // TODO Need to consider left/right direction turns
    public ArrayList<Node> getNeighbors(final Node endNode, final Heuristic heuristic) {
        final ArrayList<Node> neighbors = new ArrayList<>();

        if(xPos > 0) {
            neighbors.addAll(createNeighbors(endNode, heuristic, xPos - 1, yPos));
        }

        if(xPos < board.cols - 1) {
            neighbors.addAll(createNeighbors(endNode, heuristic, xPos + 1, yPos));
        }

        if(yPos > 0) {
            neighbors.addAll(createNeighbors(endNode, heuristic, xPos, yPos - 1));
        }

        if(yPos < board.rows - 1) {
            neighbors.addAll(createNeighbors(endNode, heuristic, xPos, yPos + 1));
        }

        return neighbors;
    }

    private ArrayList<Node> createNeighbors(final Node endNode, final Heuristic heuristic,
                                            final int xPos, final int yPos) {
        final ArrayList<Node> neighbors = new ArrayList<>();
        final Direction newDirection = Direction.compute(this.xPos, this.yPos, xPos, yPos);
        final Node newNeighbor = new Node(board, xPos, yPos, newDirection);

        // If the current node is not facing the same direction as the neighbor, a turn action is required
        if(this.getDirection() != newDirection) {
            newNeighbor.actions.addAll(Action.computeTurnDir(this.direction, newDirection));
            newNeighbor.score += turnCost(newDirection);
        }

        /*
        Determine if boost should be done on this neighbor:
        1. Current node did not just boost
        2. Neighbor has at least 3 terrain value
        3. Is current neighbor at least 2 straight spaces away from the board border?
        4. If yes, set the current node to use a boost action
         */
        boolean farXBorder = xPos >= 1 && xPos <= board.cols - 2;
        boolean movingFarFromX = farXBorder &&
                (newDirection.equals(Node.Direction.WEST) ||
                    newDirection.equals(Node.Direction.EAST));

        boolean farYBorder = yPos >= 1 && yPos <= board.rows - 2;
        boolean movingFarFromY = farYBorder &&
                (newDirection.equals(Node.Direction.NORTH) ||
                    newDirection.equals(Node.Direction.SOUTH));

        if(newNeighbor.getTerrain() > 3 && (movingFarFromX || movingFarFromY)) {
            // Add a boost action node as an option
            final Node bashNeighbor;
            switch (newDirection) {
                case NORTH -> bashNeighbor = new Node(newNeighbor.board, xPos, yPos - 1, newDirection);
                case EAST -> bashNeighbor = new Node(newNeighbor.board, xPos + 1, yPos, newDirection);
                case SOUTH -> bashNeighbor = new Node(newNeighbor.board, xPos, yPos + 1, newDirection);
                case WEST -> bashNeighbor = new Node(newNeighbor.board, xPos - 1, yPos, newDirection);
                default -> bashNeighbor = null;
            }

            bashNeighbor.score = newNeighbor.score + 3 + bashNeighbor.getTerrain();

            bashNeighbor.actions = new ArrayList<>(actions);
            bashNeighbor.actions.add(Action.BASH);
            bashNeighbor.actions.add(Action.FORWARD);

            // Add to the queue
            neighbors.add(bashNeighbor);
        }

        // Provide the option for not boosting
        newNeighbor.actions.add(Action.FORWARD);
        score += newNeighbor.getTerrain();

        // Add to list of neighbors
        neighbors.add(newNeighbor);

        // Properly define the previous node, total cost, and future cost to the new nodes
        for(Node neighbor : neighbors) {
            neighbor.prevNode = this;
            neighbor.totalCost = this.totalCost + score;
            neighbor.futureCost = totalCost + heuristic.compute(neighbor, endNode);
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

    public boolean isPathDuplicate() {
        Node currNode = this.prevNode;

        while(currNode != null) {
            if(currNode.getxPos() == this.getxPos() &&
                    currNode.getyPos() == this.getyPos()) {
                return true;
            }
            currNode = currNode.prevNode;
        }

        return false;
    }

    @Override
    public int compareTo(Node other) {
        // Output whether or not they are equal
        if (this.equals(other)) {
            return 0;
        }
        // Output whether or not the given is less than the current node
        else {
            return this.futureCost > other.futureCost ? 1 : -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;

        /*
         * See if the same actions were being performed.
         * Exclude turns, since this should only be dependent on the
         * current nodes being compared. Factoring in turns causes it
         * to be dependent on the direction of the previous node, which
         * is not necessary here.
         */
        for(final Action action : actions) {
            // skip turns
            if(action == Action.LEFT || action == Action.RIGHT) {
                continue;
            }

            // Return false if the same actions weren't being performed
            if(!node.actions.contains(action)) {
                return false;
            }
        }

        return xPos == node.xPos &&
            yPos == node.yPos &&
            terrain == node.terrain;
    }

    @Override
    public int hashCode() {
        final ArrayList<Object> hashables = new ArrayList<>();

        hashables.add(xPos);
        hashables.add(yPos);
        hashables.add(terrain);

        for(Action action : actions) {
            if(action == Action.LEFT || action == Action.RIGHT) {
                continue;
            }
            hashables.add(action);
        }

        return Objects.hash(hashables.toArray());
//        return Objects.hash(xPos, yPos, terrain, score);
    }
}
