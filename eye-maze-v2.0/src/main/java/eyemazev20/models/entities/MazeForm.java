package eyemazev20.models.entities;

import eyemazev20.Dtos.http.MazeFormDto;
import eyemazev20.utils.Point;

import java.util.LinkedList;
import java.util.Queue;

public class MazeForm {
    private final static int []dirLine = {-1, 1, 0, 0};
    private final static int []dirCol = {0, 0, 1, -1};

    private static boolean isInside(int line, int col, int nrLines, int nrCols) {
        return line >= 0 && line < nrLines && col >= 0 && col < nrCols;
    }

    public static boolean isValid(final MazeFormDto mazeFormDto) {/*
        final int nrLines = mazeFormDto.getNrLines(), nrCols = mazeFormDto.getNrCols();
        final var vis = new boolean[nrLines][nrCols];
        vis[mazeFormDto.getStart().getLine()][mazeFormDto.getStart().getCol()] = true;
        Queue<Point> queue = new LinkedList<>();
        queue.add(mazeFormDto.getStart());
        while (!queue.isEmpty()) {
            final var currP = queue.remove();
            if (currP.equals(mazeFormDto.getFinish())) {
                return true;
            }
            for (int dir = 0; dir < dirLine.length; dir++) {
                final int newLine = currP.getLine() + dirLine[dir];
                final int newCol = currP.getCol() + dirCol[dir];
                if (isInside(newLine, newCol, nrLines, nrCols)) {
                    final var currWalls = mazeFormDto.getMazeCells()[currP.getLine()][currP.getCol()].getWalls();
                    final var newWalls = mazeFormDto.getMazeCells()[newLine][newCol].getWalls();
                    var ok = false;
                    ok |= (dirLine[dir] == -1 && !currWalls[0] && !newWalls[2]);
                    ok |= (dirLine[dir] == 1 && !currWalls[2] && !newWalls[0]);
                    ok |= (dirCol[dir] == -1 && !currWalls[3] && !newWalls[1]);
                    ok |= (dirCol[dir] == 1 && !currWalls[1] && !newWalls[3]);
                    if (ok) {
                        vis[newLine][newCol] = true;
                        queue.add(new Point(newLine, newCol));
                    }
                }
            }
        }//*/
        return false;
    }
}
