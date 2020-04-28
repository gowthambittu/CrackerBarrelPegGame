import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class Main {
    public class Move {
        public int from, over, to;

        Move(int from, int over, int to) {
            this.from = from;
            this.over = over;
            this.to = to;
        }

        public Move reverse() {
            return new Move(this.to, this.over, this.from);
        }
    }

    // from,over,to : describes moves
    public Move[] moves = new Move [] {
            new Move(0,1,3),
            new Move(0,2,5),
            new Move(1,3,6),
            new Move(1,4,8),
            new Move(2,4,7),
            new Move(2,5,9),
            new Move(3,6,10),
            new Move(3,7,12),
            new Move(4,7,11),
            new Move(4,8,13),
            new Move(5,8,12),
            new Move(5,9,14),
            new Move(3,4,5),
            new Move(6,7,8),
            new Move(7,8,9),
            new Move(10,11,12),
            new Move(11,12,13),
            new Move(12,13,14)
    };

    public class Board {
        private int cells[];
        private int active;

        Board(int i) {
            this.cells = new int[15];
            for(int j=0; j<15; j++)
                cells[j] = 1;
            cells[i] = 0;
            this.active = 14;
        }

        private Board copy() {
            Board board = new Board(0);
            board.cells = this.cells.clone();
            board.active = this.active;
            return board;
        }

        public int getActive() {
            return active;
        }

        public Board makeMove(Move fot) {
            if(cells[fot.from] == 1 && cells[fot.over] == 1 && cells[fot.to] == 0) {
                Board board = this.copy();
                board.cells[fot.from] = 0; // moved away
                board.cells[fot.over] = 0; // remove jumped over
                board.cells[fot.to]   = 1; // landing here after jump
                board.active--;
                return board;
            }
            return null;
        }

        public void show() {
            int a=4, b=1, cnt=0;
            while(a >= 0) {
                for(int i=0; i<a; i++)
                    System.out.print(" ");
                for(int i=0; i<b; i++)
                    System.out.print(((cells[cnt++] == 1) ? 'X' : '.') + " ");
                System.out.println();
                a--;
                b++;
            }
        }
    }

    public class Solution {
        public Solution(int score) {
            this.moves = new ArrayList<Move>();
            this.score = score;
        }

        public Solution() {
            this.moves = new ArrayList<Move>();
            this.score = 9999;
        }

        public List<Move> moves;
        public int score;
    }

    public class Solver {
        private final Board board;

        public Solver(Board board) {
            this.board = board;
        }

        public Solution next_solution() {
            Solution solution = get_solution();
            Collections.reverse(solution.moves);
            return solution;
        }

        private Solution get_solution() {
            if(board.getActive() < 2) {
                return new Solution(board.getActive()); // empty list
            }
            Solution solution = new Solution();

            for(Move move: moves) {
                Board b = board.makeMove(move);
                if(b != null) {
                    Solver s = new Solver(b);
                    Solution sol = s.get_solution();
                    if(sol.score < solution.score)
                        solution = sol;
                    sol.moves.add(move);
                }
                b = board.makeMove(move.reverse());
                if(b != null) {
                    Solver s = new Solver(b);
                    Solution sol = s.get_solution();
                    if(sol.score < solution.score)
                        solution = sol;
                    sol.moves.add(move.reverse());
                }
            }
            return solution;
        }
    }

    public class Game {

        public Game() {}

        public List<Move> puzzle(int i) {
            Board board = new Board(i);
            Solver solver = new Solver(board);
            Solution solution = solver.next_solution();
            return solution.moves;
        }

        public void replay(List<Move> solution, int i) {
            Board board = new Board(i);
            board.show();
            for(Move m: solution)
            {
                board = board.makeMove(m);
                board.show();
                System.out.println();
            }
        }

        public void go() {
            for(int i=0; i<5; i++) {
                System.out.println("=== " + i + " ===");
                List<Move> solution = puzzle(i);
                replay(solution, i);
                System.out.println();
            }
        }
    }

    public void start()
    {
        Game game = new Game();
        game.go();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }
}
