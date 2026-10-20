import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * WHAT: Represents a state (board) of the Rush Hour puzzle.
 * WHY: To encapsulate logic for board verification, movement application, and heuristics.
 */
public class Board {

    private final String levelString;
    private final char[][] grid;
    private final List<Vehicle> vehicles; 
    private static final int BOARD_SIZE = 6;
    private static final int GOAL_ROW = 2;
    private static final int GOAL_COL = 5;

    /**
     * WHAT: Constructor for the Board class.
     * WHY: To initialize the board state from a string.
     */
    public Board(String levelString) {
        this.levelString = levelString;
        this.grid = new char[BOARD_SIZE][BOARD_SIZE];
        this.vehicles = new ArrayList<>(); 
        
        if (levelString.length() == BOARD_SIZE * BOARD_SIZE) {
            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    this.grid[r][c] = levelString.charAt(r * BOARD_SIZE + c);
                }
            }
        }
        
        if (levelString.length() == BOARD_SIZE * BOARD_SIZE) {
            List<Character> foundIds = new ArrayList<>(); 
            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    char id = this.grid[r][c];
                    if (id != 'o' && !foundIds.contains(id)) { 
                        foundIds.add(id);
                        int length = 0;
                        Orientation orientation;
                        
                        if (c + 1 < BOARD_SIZE && this.grid[r][c + 1] == id) {
                            orientation = Orientation.HORIZONTAL;
                            int temp_c = c;
                            while (temp_c < BOARD_SIZE && this.grid[r][temp_c] == id) {
                                length++;
                                temp_c++;
                            }
                        } else {
                            orientation = Orientation.VERTICAL;
                             int temp_r = r;
                            while (temp_r < BOARD_SIZE && this.grid[temp_r][c] == id) {
                                length++;
                                temp_r++;
                            }
                        }
                        this.vehicles.add(new Vehicle(id, length, orientation, r, c));
                    }
                }
            }
        }
        
        // Sort vehicles alphabetically
        Collections.sort(this.vehicles);
    }
    
    private Vehicle findVehicleById(char vehicleId) {
        Vehicle foundVehicle = null;
        for (Vehicle v : this.vehicles) {
            if (v.getId() == vehicleId) {
                foundVehicle = v;
                break;
            }
        }
        return foundVehicle;
    }

    // --- TASK 1 METHODS ---

    public int verifyLevel() {
        if (this.levelString.length() != 36) return 1;
        for (char c : this.levelString.toCharArray()) {
            if (!((c >= 'A' && c <= 'Z') || (c == 'o'))) return 2;
        }
        Vehicle redCar = findVehicleById('A');
        if (redCar == null) return 3;
        if (redCar.getRow() != GOAL_ROW) return 4;
        if (redCar.getOrientation() != Orientation.HORIZONTAL) return 5;
        for (Vehicle v : this.vehicles) {
            if (v.getLength() < 2 || v.getLength() > 3) return 6;
        }
        int cellsInString = 0;
        for(char c : this.levelString.toCharArray()) { if (c != 'o') cellsInString++; }
        int cellsInVehicles = 0;
        for (Vehicle v : this.vehicles) { cellsInVehicles += v.getLength(); }
        if (cellsInString != cellsInVehicles) return 7;
        return 0;
    }

    public String whereIs(char vehicleId) {
        Vehicle v = findVehicleById(vehicleId);
        return (v != null) ? v.getPositionsString() : "Error: Vehículo no encontrado.";
    }

    public char what(int r, int c) {
        return (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE) ? this.grid[r][c] : 'E';
    }

    public int size(char vehicleId) {
        Vehicle v = findVehicleById(vehicleId);
        return (v != null) ? v.getLength() : -1;
    }

    public int howMany() {
        return this.vehicles.size();
    }
    
    // --- TASK 2 METHODS ---
    
    public boolean isGoal() {
        Vehicle redCar = findVehicleById('A');
        if (redCar == null) return false;
        int rightmostCol = redCar.getCol() + redCar.getLength() - 1;
        return redCar.getRow() == GOAL_ROW && rightmostCol == GOAL_COL;
    }

    public String move(String actionsString) {
        Board currentBoard = this;
        String[] actions = actionsString.split(",");

        for (String action : actions) {
            char vehicleId = action.charAt(0);
            int steps = Integer.parseInt(action.substring(1));
            Vehicle v = currentBoard.findVehicleById(vehicleId);
            if (v == null) { return "Error: Vehículo no encontrado."; }
            if (!v.getValidMoves(currentBoard.grid).contains(steps)) {
                return "Error: Movimiento inválido.";
            }
            currentBoard = currentBoard.applyMove(v, steps);
        }
        return currentBoard.toString();
    }

    /**
     * WHAT: Generates the list of all successor states.
     * WHY: To implement the successor function for the solver.
     */
    public List<Successor> getSuccessors() {
        List<Successor> successors = new ArrayList<>();

        for (Vehicle v : this.vehicles) {
            List<Integer> moves = v.getValidMoves(this.grid);
            
            List<Integer> positiveMoves = new ArrayList<>();
            List<Integer> negativeMoves = new ArrayList<>();
            
            for (int m : moves) {
                if (m > 0) {
                    positiveMoves.add(m); 
                } else {
                    negativeMoves.add(m); 
                }
            }
            
            // First, POSITIVE moves
            Collections.sort(positiveMoves);
            for (int move : positiveMoves) {
                successors.add(createSuccessor(v, move));
            }

            // Second, NEGATIVE moves
            Collections.sort(negativeMoves);
            Collections.reverse(negativeMoves);
            for (int move : negativeMoves) {
                successors.add(createSuccessor(v, move));
            }
        }
        
        return successors;
    }

    private Successor createSuccessor(Vehicle v, int move) {
        String directionSign = (move > 0) ? "+" : "-";
        String action = v.getId() + directionSign + Math.abs(move);
        int cost = 6 - Math.abs(move);

        Board newBoard = this.applyMove(v, move);
        String newState = newBoard.toString();

        return new Successor(action, newState, cost);
    }

    /**
     * WHAT: Applies a single move and returns a *new* Board state.
     */
    private Board applyMove(Vehicle v, int move) {
        char[][] newGrid = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(this.grid[i], 0, newGrid[i], 0, BOARD_SIZE);
        }

        for (int[] pos : v.getPositions()) {
            newGrid[pos[0]][pos[1]] = 'o';
        }

        int newRow = v.getRow();
        int newCol = v.getCol();
        
        if (v.getOrientation() == Orientation.HORIZONTAL) {
            newCol += move; 
        } else {
            newRow -= move; 
        }

        for (int i = 0; i < v.getLength(); i++) {
            if (v.getOrientation() == Orientation.HORIZONTAL) {
                newGrid[newRow][newCol + i] = v.getId();
            } else {
                newGrid[newRow + i][newCol] = v.getId();
            }
        }
        
        return new Board(gridToString(newGrid));
    }

    private String gridToString(char[][] grid) {
        StringBuilder sb = new StringBuilder(36);
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                sb.append(grid[r][c]);
            }
        }
        return sb.toString();
    }
    
    // --- TASK 4 (Heuristics) ---
    
    /**
     * WHAT: Calculates the specified heuristic for the current state.
     * WHY: For GBF and A* strategies.
     */
    public int getHeuristic(int type) {
        return switch (type) {
            case 0 -> getHeuristic0();
            case 1 -> getHeuristic1();
            case 2 -> getHeuristic0() + getHeuristic1();
            default -> 0;
        };
    }
    
    // Heuristic 0: Number of cells to the right of red car A
    private int getHeuristic0() {
        Vehicle red = findVehicleById('A');
        if (red == null) return 0;
        
        int headCol = red.getCol() + red.getLength() - 1;
        return 5 - headCol; 
    }
    
    // Heuristic 1: Number of cars blocking the path
    private int getHeuristic1() {
        Vehicle red = findVehicleById('A');
        if (red == null) return 0;
        
        int headCol = red.getCol() + red.getLength() - 1;
        Set<Character> blockers = new HashSet<>();
        
        for (int c = headCol + 1; c < BOARD_SIZE; c++) {
            char val = this.grid[GOAL_ROW][c];
            if (val != 'o' && val != 'A') {
                blockers.add(val);
            }
        }
        return blockers.size();
    }

    @Override
    public String toString() {
        return this.levelString;
    }
}