package eyemazev20.models.entities;

import java.security.InvalidParameterException;
import java.util.Arrays;

public class MazeCell {
    private int line, col;
    private final boolean []walls;
    private boolean hasCoin;
    private final static double COIN_CH = .25;

    public void removeCoin() {
        hasCoin = false;
    }

    public int getCol() {
        return col;
    }

    public int getLine() {
        return line;
    }

    public boolean getHasCoin() {
        return hasCoin;
    }

    public MazeCell(final int line, final int col) {
        this.line = line;
        this.col = col;
        walls = new boolean[4];
        Arrays.fill(walls, true);
        hasCoin = line != 0 && col != 0 && Math.random() < COIN_CH;
    }

    private static boolean areAdj(MazeCell a, MazeCell b) {
        return Math.abs(a.line - b.line) <= 1 && Math.abs(a.col - b.col) <= 1;
    }

    public static void removeWalls(MazeCell a, MazeCell b) {
        if (!areAdj(a, b)) {
            final var errMes = "CELLS NOT ADJACENT!";
            throw new InvalidParameterException(errMes);
        }
    
        final int diffI = a.line - b.line;
        if (diffI == 1) {
            a.walls[0] = false;
            b.walls[2] = false;
        } else if (diffI == -1) {
            a.walls[2] = false;
            b.walls[0] = false;
        }
    
        final int diffJ = a.col - b.col;
        if (diffJ == 1) {
            a.walls[3] = false;
            b.walls[1] = false;
        } else if (diffJ == -1) {
            a.walls[1] = false;
            b.walls[3] = false;
        }
    }

    @Override
    public String toString() {
        return "MazeCell{" +
                "line=" + line +
                ", col=" + col +
                ", walls=" + Arrays.toString(walls) +
                ", hasCoin=" + hasCoin +
                '}';
    }

    public boolean[] getWalls() {
        return walls;
    }
}
