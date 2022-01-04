package eyemazev20.models.entities;

import eyemazev20.Services.RoomService;
import eyemazev20.utils.UtilVars;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class Game {
    final private Maze maze;
    final private Player[]players;
    public Game(Player []players, Maze maze) throws InvalidParameterException {
        assert (players.length == 2);
        this.maze = maze;
        this.players = players;
    }

    public JSONObject getGameStateAsJson(final UUID roomUUId) {
        final var state = new JSONObject();

        state.put("NR_ROWS", RoomService.uidToRoom.get(roomUUId).game.maze.getMazeCells().length);
        state.put("NR_COLS", RoomService.uidToRoom.get(roomUUId).game.maze.getMazeCells()[0].length);

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

        final var loginUUIDs = RoomService.uidToRoom.get(roomUUId).getPlUUIDs();
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
            } else {
                return;
            }
            if (col >= 0 && col < maze.getMazeCells()[0].length) {
                players[playerNr].setCol(col);
            } else {
                return;
            }
            validMove.set(true);
        };

        int line = players[playerNr].getLine(), col = players[playerNr].getCol();
        switch (dir) {
            case UP: if (!maze.getMazeCells()[line][col].getWalls()[0]) line--; break;
            case DOWN: if (!maze.getMazeCells()[line][col].getWalls()[2]) line++; break;
            case LEFT: if (!maze.getMazeCells()[line][col].getWalls()[3]) col--; break;
            case RIGHT: if (!maze.getMazeCells()[line][col].getWalls()[1]) col++; break;
            default: throw new InvalidParameterException("INVALID DIR!");
        }
        if (!(players[playerNr].getLine() == line && players[playerNr].getCol() == col)) {
            utilMove.accept(line, col);
        }
        if (validMove.get() && maze.getMazeCells()[line][col].getHasCoin()) {
            maze.getMazeCells()[line][col].removeCoin();
            players[playerNr].incrementCoins();
        }
        if (line == maze.getEnd().getLine() && col == maze.getEnd().getCol()) {
            players[playerNr].finish();
            Timer timer = new Timer();
            Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
            calendar.add(Calendar.SECOND, 5);
            Date date = calendar.getTime();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    players[playerNr ^ 1].finish();
                }
            }, date);
        }
        return validMove.get();
    }
    public Player[] getPlayers() {
        return players;
    }
}
