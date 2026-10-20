/**
 * WHAT: Stores a successor state.
 * WHY: To represent the tuple <action, newState, cost> required by the successor function.
 */
public class Successor {
    private final String action;
    private final String newState;
    private final int cost;

    /**
     * WHAT: Constructor for the Successor class.
     * WHY: To create a new successor tuple.
     */
    public Successor(String action, String newState, int cost) {
        this.action = action;
        this.newState = newState;
        this.cost = cost;
    }
    
    public String getAction() {
        return this.action;
    }
    
    public String getNewState() {
        return this.newState;
    }
    
    public int getCost() {
        return this.cost;
    }
    
    /**
     * WHAT: Returns the string representation of the successor.
     * WHY: To match the output format [action,newState,cost].
     */
    @Override
    public String toString() {
        String representation = "[" + this.action + "," + this.newState + "," + this.cost + "]";
        return representation;
    }
}