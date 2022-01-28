package eyemazev20.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import eyemazev20.Dtos.http.PastGameDto;
import eyemazev20.Dtos.http.StringDto;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.entities.Game;
import eyemazev20.models.entities.Maze;
import eyemazev20.models.entities.Player;
import eyemazev20.models.orm.MazeOrm;
import eyemazev20.models.orm.PastGame;
import eyemazev20.models.orm.User;
import eyemazev20.utils.Point;
import eyemazev20.utils.UtilVars;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.dialect.PostgreSQL94Dialect;

@Service
public class GameService {
    public static void initGame(final UUID roomUUID) throws Exception {
        int playerStartI = 0, playerStartJ = 0;


        int nrLines = UtilVars.DEFAULT_MAZE_LEN, nrCols = UtilVars.DEFAULT_MAZE_LEN;

        final var mazeParams = RoomService.uidToRoom.get(roomUUID).getMazeParams();
        if (mazeParams != null) {
            nrLines = mazeParams.getNrLines();
            nrCols = mazeParams.getNrCols();
        }

        final var way = MazeGenService.genMaze(nrLines, nrCols);

        Maze maze;
        final var mzName = RoomService.uidToRoom.get(roomUUID).getMzName();
        int mazeId;
        if (mzName == null) {
            maze = new Maze(nrLines, nrCols, way,
                    new Point(), new Point(nrLines - 1, nrCols - 1));

            final var dbMz = maze.toDbObj();
            mazeId = MazeServices.saveMazeToDb(
                    new MazeOrm(dbMz.getKey().toString(), dbMz.getValue(), null)
            );
        } else {
            final MazeOrm mzOrm = MazeServices.getDataByName(mzName);
            mazeId = mzOrm.getId();
            maze = new Maze(mzOrm.getForm());
            playerStartI = maze.getStart().getLine();
            playerStartJ = maze.getStart().getCol();
        }
        var players = new Player[] {new Player(playerStartI, playerStartJ), new Player(playerStartI, playerStartJ)};
        RoomService.uidToRoom.get(roomUUID).game = new Game(players, maze, mazeId);
    }

    public static boolean movePlayer(final UUID roomUUId, final String dir, int playerNr) {
        if (RoomService.uidToRoom.get(roomUUId).game.getPlayers()[playerNr].hadFinished()) {
            return false;
        }
        if (dir.isEmpty() || dir.equalsIgnoreCase("afk")) {
            return false;
        }
        return RoomService.uidToRoom.get(roomUUId).game.move(Player.DIRS.valueOf(dir), playerNr);
    }

    public static void finishGame(StringDto gameState, UUID roomUUID) {
        gameState.setBuffer("OVER! " + roomUUID);
        GameService.addGameToPastGames(roomUUID);
        RoomService.uidToRoom.remove(roomUUID);
    }

    public static void addGameToPastGames(final UUID roomUUID) {
        final var game = RoomService.uidToRoom.get(roomUUID).game;

        final List<Integer> coins = new ArrayList<>();
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
                    coins.stream().mapToInt(coin -> coin).toArray(),
                    game.getMazeId()
            );
            UtilVars.session.save(pastGame);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
    }

    public static PastGameDto getPastGameData(UUID roomUUID, String loginUUID) {
        final var qs = "SELECT " +
                "{pg.*}, {u.*}, {u2.*}, {mz.*} " +
                "FROM PastGames pg NATURAL JOIN Mazes mz " +
                "JOIN Users u ON(u.loginUUID = ANY(pg.pluuids)) \n" +
                "JOIN Users u2 ON(u2.loginUUID = ANY(pg.pluuids))\n" +
                "WHERE pg.roomUUID = :roomUUID AND u.username <> u2.username AND u.loginUUID = :loginUUID";
        ;
        final var query = UtilVars.session.createSQLQuery(qs);//Query(qs);

        query.addEntity("u", User.class);
        query.addEntity("u2", User.class);
        query.addEntity("pg", PastGame.class);
        query.addEntity("mz", MazeOrm.class);

        query.setParameter("roomUUID", roomUUID.toString());
        query.setParameter("loginUUID", loginUUID);

        final List<Object[]> list = query.list();
        final var res = list.get(0);
        final var u = (User) res[0];
        final var u2 = (User) res[1];
        final var pg = (PastGame) res[2];
        final var mz = (MazeOrm) res[3];
        System.out.println(u.getUsername() + "---"  + u2.getUsername());
        System.out.println(pg.getScores()[0] + "---" + pg.getScores()[1]);
        return new PastGameDto(
                new String[] {
                        u.getUsername(),
                        u2.getUsername()
                },
                new int[] {
                        pg.getScores()[0],
                        pg.getScores()[1]
                },
                pg.getTimestp(),
                mz.getName(),
                new String[] {
                        u.getProfilePicB64(),
                        u2.getProfilePicB64()
                }
        );//*/
    }

    public static ArrayList<PastGame> getPastGamesOfUser(String loginUUID) {
        final var qs = "SELECT {pg.*} FROM PastGames AS pg WHERE :loginUUID = ANY(pg.plUUIDs)";
        final var query = UtilVars.session.createSQLQuery(qs);
        query.setParameter("loginUUID", loginUUID);
        query.addEntity("pg", PastGame.class);
        return (ArrayList<PastGame>) query.list();
    }//*/
}
