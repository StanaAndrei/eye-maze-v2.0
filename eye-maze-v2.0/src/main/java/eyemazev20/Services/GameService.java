package eyemazev20.Services;

import eyemazev20.Dtos.StringDto;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.Game;
import eyemazev20.models.Maze;
import eyemazev20.models.Player;
import eyemazev20.models.orm.PastGame;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    public static void initGame(final UUID roomUUID) {
        var players = new Player[] {new Player(0, 0), new Player(0, 0)};

        final var way = MazeGenService.genMaze(UtilVars.MAZE_LEN, UtilVars.MAZE_LEN);
        final var maze = new Maze(UtilVars.MAZE_LEN, UtilVars.MAZE_LEN, way);
        RoomService.uidToRoom.get(roomUUID).game = new Game(players, maze);
    }

    public static boolean movePlayer(final UUID roomUUId, final String dir, int playerNr) {
        if (RoomService.uidToRoom.get(roomUUId).game.getPlayers()[playerNr].hadFinished()) {
            return false;
        }

        return RoomService.uidToRoom.get(roomUUId).game.move(Player.DIRS.valueOf(dir), playerNr);
    }

    public static void addGameToPastGames(final UUID roomUUID) {
        final var game = RoomService.uidToRoom.get(roomUUID).game;

        List<Integer> coins = new ArrayList<>();
        for (final var player : game.getPlayers()) {
            coins.add(player.getCoins());
        }

        Transaction transaction = null;
        assert (!roomUUID.toString().isEmpty());
        try {
            transaction = UtilVars.session.beginTransaction();
            final var pastGame = new PastGame(
                    roomUUID.toString(),
                    RoomService.uidToRoom.get(roomUUID).getPlUUIDs(),
                    coins.stream().mapToInt(coin -> coin).toArray()
            );
            UtilVars.session.save(pastGame);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
    }

    public static PastGame getPastGameData(UUID roomUUID) {
        final var qs = "SELECT pg.roomUUID, pg.pluuids, pg.scores FROM PastGames pg WHERE pg.roomUUID = :roomUUID";
        final var query = UtilVars.session.createSQLQuery(qs);//Query(qs);
        query.setParameter("roomUUID", roomUUID.toString());
        query.addEntity("pg", PastGame.class);
        PastGame res = ((List<PastGame>) query.list()).get(0);
        return res;
    }
}
