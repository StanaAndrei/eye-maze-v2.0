package eyemazev20.Models.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.util.Arrays;

@NoArgsConstructor
public class MazeCell {
    private int line, col;
    private boolean []walls;
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

    public MazeCell(String mazeCellAsJson) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject(mazeCellAsJson);
        this.line = jsonObject.getInt("line");
        this.col = jsonObject.getInt("col");
        this.hasCoin = jsonObject.getBoolean("hasCoin");
        final var wallsAsJson = jsonObject.getJSONArray("walls");
        walls = new boolean[4];
        for (int i = 0; i < wallsAsJson.length(); i++) {
            this.walls[i] = wallsAsJson.getBoolean(i);
        }
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("line", line);
        jsonObject.put("col", col);
        jsonObject.put("hasCoin", hasCoin);
        jsonObject.put("walls", walls);
        return jsonObject.toString();
    }

    public boolean[] getWalls() {
        return walls;
    }
}
