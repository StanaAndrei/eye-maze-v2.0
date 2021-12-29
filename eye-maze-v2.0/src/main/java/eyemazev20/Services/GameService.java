package eyemazev20.Services;

import eyemazev20.models.Game;
import eyemazev20.models.Maze;
import eyemazev20.models.Player;
import eyemazev20.models.Room;
import eyemazev20.utils.UtilVars;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {
    public static void initGame(final UUID roomUUID) {
        var players = new Player[] {
                new Player(0, 0),
                new Player(0, 0)
        };
        final var way = MazeGenService.genMaze(UtilVars.MAZE_LEN, UtilVars.MAZE_LEN);
        final var maze = new Maze(UtilVars.MAZE_LEN, UtilVars.MAZE_LEN, way);
        RoomService.uidToRoom.get(roomUUID).game = new Game(players, maze);
    }

    public static boolean movePlayer(final UUID roomUUId, final String dir, int playerNr) {
        return RoomService.uidToRoom.get(roomUUId).game.move(Player.DIRS.valueOf(dir), playerNr);
    }
}
