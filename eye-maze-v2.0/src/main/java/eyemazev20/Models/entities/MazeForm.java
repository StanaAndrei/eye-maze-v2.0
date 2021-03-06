package eyemazev20.Models.entities;

import eyemazev20.Dtos.http.MazeFormDto;
import eyemazev20.Utils.Point;

import java.util.LinkedList;
import java.util.Queue;

public class MazeForm {
    private final static int []dirLine = {-1, 1, 0, 0};
    private final static int []dirCol = {0, 0, 1, -1};

    private static boolean isInside(int line, int col, int nrLines, int nrCols) {
        return line >= 0 && line < nrLines && col >= 0 && col < nrCols;
    }

    public static boolean isValid(final MazeFormDto mazeFormDto) {
        final int nrLines = mazeFormDto.getNrLines(), nrCols = mazeFormDto.getNrCols();
        final var vis = new boolean[nrLines][nrCols];
        vis[mazeFormDto.getStart().getLine()][mazeFormDto.getStart().getCol()] = true;
        Queue<Point> queue = new LinkedList<>();
        queue.add(mazeFormDto.getStart());
        //map 1d -> 2d
        MazeCell [][]mazeCells = new MazeCell[nrLines][nrCols];
        for (int i = 0, nr = 0; i < nrLines; i++) {
            for (int j = 0; j < nrCols; j++) {
                mazeCells[i][j] = mazeFormDto.getMazeCells()[nr++];
            }
        }

        while (!queue.isEmpty()) {
            final var currP = queue.remove();
            if (currP.equals(mazeFormDto.getFinish())) {
                return true;
            }
            for (int dir = 0; dir < dirLine.length; dir++) {
                final int newLine = currP.getLine() + dirLine[dir];
                final int newCol = currP.getCol() + dirCol[dir];
                if (isInside(newLine, newCol, nrLines, nrCols) && !vis[newLine][newCol]) {
                    final var currWalls = mazeCells[currP.getLine()][currP.getCol()].getWalls();
                    final var newWalls = mazeCells[newLine][newCol].getWalls();
                    var ok = false;
                    ok |= (dirLine[dir] == -1 && !currWalls[0] && !newWalls[2]);
                    ok |= (dirLine[dir] == 1 && !currWalls[2] && !newWalls[0]);
                    ok |= (dirCol[dir] == -1 && !currWalls[3] && !newWalls[1]);
                    ok |= (dirCol[dir] == 1 && !currWalls[1] && !newWalls[3]);
                    if (ok) {
                        vis[newLine][newCol] = true;
                        queue.add(new Point(newLine, newCol)); //@ERROR: "java.lang.OutOfMemoryError: Java heap space"
                    }
                }
            }
        }//*/

        return false;
    }
}
