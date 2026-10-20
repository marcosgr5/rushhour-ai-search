# Rush Hour Solver (Java)

**ES** · [EN](#english)

Solucionador del puzzle **Rush Hour** en Java mediante búsqueda en espacio de estados. Proyecto académico de la Escuela Superior de Informática (UCLM).

## Qué incluye

- Tablero de 6x6 representado como una cadena de 36 caracteres: `o` es una casilla vacía, cada letra es un vehículo y `A` es el coche rojo. La salida está en el borde derecho de la tercera fila.
- Estrategias de búsqueda: `BFS` (anchura), `DFS` (profundidad, con límite opcional `--depth`), `UCS` (coste uniforme), `GBF` (voraz) y `AStar` (A*).
- Heurísticas (`--heuristic`): `0` casillas entre el coche rojo y la salida, `1` vehículos que bloquean el camino y `2` la suma de ambas.
- Acciones adicionales: `verify`, `question` y `successors`.

## Uso

Compilar y resolver un nivel con A*:

```
javac *.java
java RushHour solver -s ooooooooooBooAAoBoooooooooooooooooooo --strategy AStar --heuristic 2
```

Cada línea de salida es un nodo del camino solución con el formato `[id,padre,acción,estado,coste,profundidad,heurística,valor]`. Con `--stats` se añaden las estadísticas de la búsqueda.

## Créditos

Autor: **Marcos Galán Rodríguez**. El enunciado, la especificación del formato de entrada y salida y los tests de evaluación pertenecen a los profesores de la asignatura y no se incluyen en este repositorio.

---

## English

Java solver for the **Rush Hour** sliding-block puzzle using state-space search. Academic project from the School of Computer Science (UCLM, Spain).

### Features

- 6x6 board encoded as a 36-character string: `o` is an empty cell, each letter is a vehicle and `A` is the red car. The exit is on the right edge of the third row.
- Search strategies: `BFS`, `DFS` (optional depth limit `--depth`), `UCS` (uniform cost), `GBF` (greedy best-first) and `AStar` (A*).
- Heuristics (`--heuristic`): `0` cells between the red car and the exit, `1` vehicles blocking the path, `2` the sum of both.
- Extra actions: `verify`, `question` and `successors`.

### Usage

```
javac *.java
java RushHour solver -s ooooooooooBooAAoBoooooooooooooooooooo --strategy AStar --heuristic 2
```

Each output line is a node of the solution path: `[id,parent,action,state,cost,depth,heuristic,value]`. Add `--stats` to print search statistics.

### Credits

Author: **Marcos Galán Rodríguez**. The assignment text, the input/output format specification and the grading tests belong to the course instructors and are not included here.
