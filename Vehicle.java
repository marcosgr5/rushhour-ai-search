import java.util.List;
import java.util.ArrayList;

enum Orientation {
    HORIZONTAL,
    VERTICAL
}

/**
 * WHAT: Represents a single vehicle on the board.
 * WHY: To encapsulate the vehicle's state (ID, size, orientation, position) and simplify logic.
 */
public class Vehicle implements Comparable<Vehicle> {

    private final char id;
    private final int length;
    private final Orientation orientation;
    private final int row;
    private final int col;

    public Vehicle(char id, int length, Orientation orientation, int row, int col) {
        this.id = id;
        this.length = length;
        this.orientation = orientation;
        this.row = row;
        this.col = col;
    }

    public char getId() { return id; }
    public int getLength() { return length; }
    public Orientation getOrientation() { return orientation; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public List<int[]> getPositions() {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < this.length; i++) {
            if (this.orientation == Orientation.HORIZONTAL) {
                positions.add(new int[]{this.row, this.col + i});
            } else {
                positions.add(new int[]{this.row + i, this.col});
            }
        }
        return positions;
    }

    public String getPositionsString() {
        StringBuilder sb = new StringBuilder();
        for (int[] pos : getPositions()) {
            sb.append("(").append(pos[0]).append(",").append(pos[1]).append(")");
        }
        String positionString = sb.toString();
        return positionString;
    }

    /**
     * WHAT: Checks valid moves for this vehicle on a grid.
     * WHY: Required for generating successors.
     */
    public List<Integer> getValidMoves(char[][] grid) {
        List<Integer> moves = new ArrayList<>();
        int boardSize = 6;

        if (this.orientation == Orientation.HORIZONTAL) {
            // Horizontal (Positive, Right)
            int headCol = this.col + this.length - 1;
            for (int i = 1; (headCol + i) < boardSize; i++) {
                if (grid[this.row][headCol + i] == 'o') {
                    moves.add(i);
                } else {
                    break;
                }
            }
        } else {
            // Vertical (Negative, Down)
            int headRow = this.row + this.length - 1;
            for (int i = 1; (headRow + i) < boardSize; i++) {
                if (grid[headRow + i][this.col] == 'o') {
                    moves.add(-i);
                } else {
                    break;
                }
            }
        }

        if (this.orientation == Orientation.HORIZONTAL) {
            // Horizontal (Negative, Left)
            for (int i = 1; (this.col - i) >= 0; i++) {
                if (grid[this.row][this.col - i] == 'o') {
                    moves.add(-i);
                } else {
                    break;
                }
            }
        } else {
            // Vertical (Positive, Up)
            for (int i = 1; (this.row - i) >= 0; i++) {
                if (grid[this.row - i][this.col] == 'o') {
                    moves.add(i);
                } else {
                    break;
                }
            }
        }
        return moves;
    }
    
    @Override
    public int compareTo(Vehicle other) {
        int comparison = Character.compare(this.id, other.id);
        return comparison;
    }
}