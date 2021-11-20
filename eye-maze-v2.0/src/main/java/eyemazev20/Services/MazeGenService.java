package eyemazev20.Services;

import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.stream.IntStream;

@Service
public
class MazeGenService {
    private static boolean isInside(int i, int j, int n, int m) {
        return 0 <= i && i < n && 0 <= j && j < m;
    }

    private static class Cell {
        public int line, col;
        public Cell(int line, int col) {
            this.line = line;
            this.col = col;
        }

        @Override
        public String toString() {
            return "Cell{" +
                    "line=" + line +
                    ", col=" + col +
                    '}';
        }
    }

    private static Cell getNeighbour(Cell currCell, int [][]vis, int n, int m) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        Cell top, bottom, left, right;
        top = bottom = left = right = null;

        if (isInside(currCell.line - 1, currCell.col, n, m)) {
            top = new Cell(currCell.line - 1, currCell.col);
        }
        if (isInside(currCell.line + 1, currCell.col, n, m)) {
            bottom = new Cell(currCell.line + 1, currCell.col);
        }
        if (isInside(currCell.line, currCell.col - 1, n, m)) {
            left = new Cell(currCell.line, currCell.col - 1);
        }
        if (isInside(currCell.line, currCell.col + 1, n, m)) {
            right = new Cell(currCell.line, currCell.col + 1);
        }

        if (top != null && vis[top.line][top.col] != 2) {
            neighbours.add(top);
        }
        if (bottom != null && vis[bottom.line][bottom.col] != 2) {
            neighbours.add(bottom);
        }
        if (left != null && vis[left.line][left.col] != 2) {
            neighbours.add(left);
        }
        if (right != null && vis[right.line][right.col] != 2) {
            neighbours.add(right);
        }

        if (neighbours.size() != 0) {
            final var random = new Random();
            final IntStream intStream = random.ints(0, neighbours.size());
            final int randPos = intStream.findFirst().getAsInt();
            return neighbours.get(randPos);
        }
        return null;
    }

    private static boolean areAllVis(final int [][]vis) {
        for (var line : vis) {
            for (int field : line) {
                if (field == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Cell> genMaze(int n, int m) {
        ArrayList<Cell> mazeWay = null;
        Stack<Cell> stack = new Stack<>();
        int nrVis;
        boolean genFinished;
        int [][]vis;
        do {
            nrVis = 1;
            mazeWay = new ArrayList<>();
            var currCell = new Cell(0, 0);
            vis = new int[n][m];
            vis[currCell.line][currCell.col]++;
            genFinished = (nrVis > n * m && stack.isEmpty());
            while (!genFinished) {
                mazeWay.add(currCell);
                final var nextCell = MazeGenService.getNeighbour(
                        currCell,
                        vis,
                        n, m
                );
                if (nextCell != null) {
                    vis[nextCell.line][nextCell.col]++;
                    nrVis++;
                    stack.push(currCell);
                    currCell = nextCell;
                } else if (!stack.isEmpty()) {
                    currCell = stack.pop();
                }//*/
                genFinished = (nrVis > n * m && stack.isEmpty());
            }
        } while (!MazeGenService.areAllVis(vis));//*/
        return mazeWay;
    }
}
