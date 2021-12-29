package eyemazev20.models;

import eyemazev20.Services.MazeGenService;
import java.util.ArrayList;

public class Maze {
    private MazeCell [][]mazeCells;

    public MazeCell[][] getMazeCells() {
        return mazeCells;
    }

    public Maze(int n, int m, ArrayList<MazeGenService.Cell> way) {
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
