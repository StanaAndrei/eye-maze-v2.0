package eyemazev20.models;

import eyemazev20.Services.RoomService;
import eyemazev20.utils.UtilVars;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class Game {
    private Maze maze;
    private Player []players;
    public Game(Player []players, Maze maze) throws InvalidParameterException {
        if (players.length != 2) {
            throw new InvalidParameterException();
        }
        this.maze = maze;
        this.players = players;
    }

    public JSONObject getGameStateAsJson(final UUID roomUUId) {
        final var state = new JSONObject();

        state.put("NR_ROWS", UtilVars.MAZE_LEN);
        state.put("NR_COLS", UtilVars.MAZE_LEN);

        final var loginUUIDs = RoomService.uidToRoom.get(roomUUId).getPlUUIDs();

        final var mazeState = new JSONArray();
        for (final var line : maze.getMazeCells()) {
            for (final var mazeCell : line) {
                final var cellData = new JSONObject();
                cellData.put("hasCoin", mazeCell.getHasCoin());
                cellData.put("line", mazeCell.getLine());
                cellData.put("col", mazeCell.getCol());

                final var walls = new JSONArray();
                for (final boolean wall : mazeCell.getWalls()) {
                    walls.put(wall);
                }
                cellData.put("walls", walls);

                mazeState.put(cellData);
            }
        }
        state.put("mazeState", mazeState);

        final var playersAsJson = new JSONArray();
        for (int i = 0; i < players.length; i++) {
            JSONObject playerAsJson = new JSONObject();
            playerAsJson.put("line", players[i].getLine());
            playerAsJson.put("col", players[i].getCol());
            playerAsJson.put("plUUID", loginUUIDs[i]);
            playersAsJson.put(playerAsJson);
        }
        state.put("players", playersAsJson);

        return state;
    }

    public boolean move(Player.DIRS dir, int playerNr) {
        AtomicBoolean validMove = new AtomicBoolean(false);
        final BiConsumer<Integer, Integer> utilMove = (line, col) -> {
            if (line >= 0 && line < maze.getMazeCells().length) {
                players[playerNr].setLine(line);
            }
            if (col >= 0 && col < maze.getMazeCells()[0].length) {
                players[playerNr].setCol(col);
            }
            validMove.set(true);
        };

        final int line = players[playerNr].getLine();
        final int col = players[playerNr].getCol();
        switch (dir) {
            case UP: if (!maze.getMazeCells()[line][col].getWalls()[0]) utilMove.accept(line - 1, col); break;
            case DOWN: if (!maze.getMazeCells()[line][col].getWalls()[2]) utilMove.accept(line + 1, col); break;
            case LEFT: if (!maze.getMazeCells()[line][col].getWalls()[3]) utilMove.accept(line, col - 1); break;
            case RIGHT: if (!maze.getMazeCells()[line][col].getWalls()[1]) utilMove.accept(line, col + 1); break;
            default: throw new InvalidParameterException("INVALID DIR!");
        }

        return validMove.get();
    }
}
