import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * WHAT: Implements the general search algorithm.
 * WHY: To encapsulate the solver logic for BFS, DFS, UCS, GBF, and AStar strategies.
 */
public class Solver {

    private long et = 0; 
    private int tn = 0;  
    private int en = 0;  
    private int cn = 0; 
    private int df = 0;  

    /**
     * WHAT: Executes the search algorithm.
     * @return The goal node if found, or null.
     */
    public Node solve(String initialState, String strategy, int maxDepth, int heuristicType) {
        long startTime = System.nanoTime();
        
        Node.resetIdCounter();
        tn = 0;
        en = 0;
        cn = 0;
        df = 0;

        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Map<String, Integer> visited = new HashMap<>();

        Board initialBoard = new Board(initialState);
        
        // Force heuristic to 0 for uninformed strategies (BFS, DFS, UCS)
        boolean isUninformed = strategy.equals("BFS") || strategy.equals("DFS") || strategy.equals("UCS");
        int initialH = isUninformed ? 0 : initialBoard.getHeuristic(heuristicType);

        Node root = new Node(null, null, initialState, 0, initialH, strategy);
        
        // Initialize df with root depth
        df = root.getDepth();

        frontier.add(root);
        tn = 1; 

        Node solutionNode = null;

        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll();

            // --- 1. PRUNING ---
            if (visited.containsKey(currentNode.getState())) {
                int oldDepth = Math.abs(visited.get(currentNode.getState()));
                int currentDepth = Math.abs(currentNode.getValue()); 

                if (oldDepth <= currentDepth) {
                    cn++; 
                    continue;
                }
            }

            // --- 2. UPDATE CLOSED LIST ---
            visited.put(currentNode.getState(), currentNode.getValue());

            // --- 3. GOAL TEST ---
            Board currentBoard = new Board(currentNode.getState());
            if (currentBoard.isGoal()) {
                solutionNode = currentNode;
                break;
            }

            // --- 4. DEPTH CHECK (DFS) ---
            if (strategy.equals("DFS") && maxDepth > -1 && currentNode.getDepth() >= maxDepth) {
                cn++;
                continue;
            }

            // --- 5. EXPANSION ---
            en++; 
            List<Successor> successors = currentBoard.getSuccessors();

            for (Successor s : successors) {
                Board childBoard = new Board(s.getNewState());
                
                // Force heuristic to 0 for child nodes if strategy is uninformed
                int childH = isUninformed ? 0 : childBoard.getHeuristic(heuristicType);
                
                Node child = new Node(
                    currentNode, 
                    s.getAction(), 
                    s.getNewState(), 
                    s.getCost(), 
                    childH,
                    strategy
                );
                
                tn++; 

                // Update Max Depth (df) upon generation
                if (child.getDepth() > df) {
                    df = child.getDepth();
                }

                frontier.add(child);
            }
        }
        
        long endTime = System.nanoTime();
        this.et = (endTime - startTime) / 1000; 

        return solutionNode;
    }

    public String getStatsString() {
        return String.format("ET: %d\nTN: %d\nEN: %d\nCN: %d\nDF: %d",
                this.et, this.tn, this.en, this.cn, this.df);
    }
}