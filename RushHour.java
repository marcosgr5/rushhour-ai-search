import java.util.List;

/**
 * WHAT: Main entry point for the RushHour program.
 * WHY: Handles command line arguments for the different tasks (solver, verify, question).
 */
public class RushHour {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: No se especificó ninguna acción.");
            System.err.println("Uso: java RushHour <action> [params]");
            return;
        }

        String action = args[0];
        
        if (!action.equals("solver")) {
            if (args.length < 3 || !args[1].equals("-s")) {
                System.err.println("Error: Argumentos insuficientes o mal formados.");
                System.err.println("Uso: java RushHour <action> -s <level_string> [params]");
                return;
            }
            
            String levelString = args[2];
            Board board = new Board(levelString);

            switch (action) {
                case "verify" -> System.out.println(board.verifyLevel());
                case "question" -> handleQuestions(board, args);
                case "successors" -> {
                    List<Successor> successors = board.getSuccessors();
                    for (Successor s : successors) {
                        System.out.println(s);
                    }
                }
                default -> System.err.println("Error: Acción '" + action + "' desconocida.");
            }
        } else {
            handleSolver(args);
        }
    }
    
    /**
     * WHAT: Parses arguments for 'solver' and executes the search.
     */
    private static void handleSolver(String[] args) {
        String level = "";
        String strategy = "";
        int depth = -1; 
        int heuristic = 0; // Default 0
        boolean stats = false;

        try {
            for (int i = 1; i < args.length; i++) {
                switch (args[i]) {
                    case "-s" -> level = args[++i];
                    case "--strategy" -> strategy = args[++i];
                    case "--depth" -> depth = Integer.parseInt(args[++i]);
                    case "--heuristic" -> heuristic = Integer.parseInt(args[++i]);
                    case "--stats" -> stats = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al parsear argumentos de 'solver': " + e.getMessage());
            return;
        }

        if (level.isEmpty() || strategy.isEmpty()) {
            System.err.println("Error: 'solver' requiere al menos -s <level> y -strategy <strategy>.");
            return;
        }
        
        Solver solver = new Solver();
        Node solutionNode = solver.solve(level, strategy, depth, heuristic);

        if (solutionNode != null) {
            List<Node> path = solutionNode.getPath();
            for (Node n : path) {
                System.out.println(n);
            }
        } else {
            System.out.println("No se encontró solución.");
        }

        if (stats) {
            System.out.println(solver.getStatsString());
        }
    }

    private static void handleQuestions(Board board, String[] args) {
        if (args.length < 4) {
             System.err.println("Error: Faltan parámetros para 'question'. Se esperaba '--questiontype'.");
             return;
        }

        String questionType = args[3];
        String param = "";
        
        if (args.length > 4) {
            param = args[4];
        }

        try {
            switch (questionType) {
                case "--whereis" -> System.out.println(board.whereIs(param.charAt(0)));
                case "--what" -> {
                    String[] coords = param.split(",");
                    int r = Integer.parseInt(coords[0]);
                    int c = Integer.parseInt(coords[1]);
                    System.out.println(board.what(r, c));
                }
                case "--size" -> System.out.println(board.size(param.charAt(0)));
                case "--howmany" -> System.out.println(board.howMany());
                    
                case "--goal" -> System.out.println(board.isGoal() ? "TRUE" : "FALSE");
                case "--move" -> System.out.println(board.move(param));
                    
                default -> System.err.println("Error: Pregunta '" + questionType + "' desconocida.");
            }
        } catch (Exception e) {
            System.err.println("Error procesando la pregunta: " + e.getMessage());
        }
    }
}