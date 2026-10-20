import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * WHAT: Represents a node in the search tree.
 * WHY: To encapsulate state, cost, depth, heuristic, and total value for the search algorithms.
 */
public class Node implements Comparable<Node> {

    private static int nextId = 0;

    private final int id;
    private final Node parent;
    private final String action; 
    private final String state;
    private final int cost;
    private final int depth;
    private final int heuristic;
    private final int value; 

    public Node(Node parent, String action, String state, int stepCost, int heuristic, String strategy) {
        this.id = nextId++;
        this.parent = parent;
        this.state = state;
        this.heuristic = heuristic;

        if (parent == null) {
            // Root node
            this.action = "___"; 
            this.depth = 0;
            this.cost = 0;
        } else {
            // Child node
            this.action = action;
            this.depth = parent.depth + 1; 
            this.cost = parent.cost + stepCost; 
        }

        // Assign node value based on strategy
        switch (strategy) {
            case "BFS" -> this.value = this.depth; 
            case "DFS" -> this.value = -this.depth; // Negative for LIFO behavior in PQ
            case "UCS" -> this.value = this.cost; 
            case "GBF" -> this.value = this.heuristic; 
            case "AStar" -> this.value = this.cost + this.heuristic; // f = g + h
            default -> this.value = this.depth; 
        }
    }

    public static void resetIdCounter() {
        nextId = 0;
    }

    public String getState() { return state; }
    public int getDepth() { return depth; }
    public int getCost() { return cost; }
    public int getId() { return id; }
    public int getValue(){ return value;}
    
    public List<Node> getPath() {
        List<Node> path = new ArrayList<>();
        Node current = this;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * WHAT: Compares this node with another.
     * WHY: Required for PriorityQueue ordering in the Frontier.
     */
    @Override
    public int compareTo(Node other) {
        int valueComparison = Integer.compare(this.value, other.value);
        if (valueComparison != 0) {
            return valueComparison;
        }
        return Integer.compare(this.id, other.id);
    }

    /**
     * WHAT: Returns the string representation of the node.
     * WHY: To match the required output format.
     */
    @Override
    public String toString() {
        String parentIdStr = (this.parent == null) ? "none" : String.valueOf(this.parent.id);
        
        return String.format("[%d,%s,%s,%s,%d,%d,%d,%d]",
                this.id,
                parentIdStr,
                this.action,
                this.state,
                this.cost,
                this.depth,
                this.heuristic,
                this.value
        );
    }
}