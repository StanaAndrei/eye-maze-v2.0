package eyemazev20.models.entities;

import eyemazev20.Services.MazeGenService;
import eyemazev20.utils.Point;

import java.util.ArrayList;

public class Maze {
    private MazeCell[][]mazeCells;
    private Point start, end;

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public MazeCell[][] getMazeCells() {
        return mazeCells;
    }

    public Maze(int n, int m, ArrayList<MazeGenService.Cell> way, Point start, Point end) {
        assert (start.getLine() >= 0 && start.getCol() >= 0);
        assert (end.getLine() < n && end.getCol() < m);
        this.start = start;
        this.end = end;
        mazeCells = new MazeCell[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mazeCells[i][j] = new MazeCell(i, j);
            }
        }

        for (int i = 1; i < way.size(); i++) {
            final int lastLine = way.get(i - 1).line;
            final int lastCol = way.get(i - 1).col;

            final int currLine = way.get(i).line;
            final int currCol = way.get(i).col;

            MazeCell.removeWalls(mazeCells[lastLine][lastCol], mazeCells[currLine][currCol]);
        }
    }
}
