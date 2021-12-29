package eyemazev20.models;

import java.security.InvalidParameterException;
import java.util.function.BiConsumer;

public class Player {
    public enum DIRS {
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }
    private int line, col;

    public Player(final int line, final int col) {
        this.line = line;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getLine() {
        return line;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
