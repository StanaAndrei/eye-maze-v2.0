package eyemazev20.Services;

import eyemazev20.models.Room;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class RoomService {
    public static HashMap<UUID, Room> uidToRoom = new HashMap<>();

    public static boolean canBeJoined(UUID roomUuid) {
        boolean ok = false;
        ok |= RoomService.uidToRoom.containsKey((roomUuid));
        if (ok) {
            ok = false;
            ok |= RoomService.uidToRoom.get((roomUuid)).getPlayers()[0] == null;
            ok |= RoomService.uidToRoom.get((roomUuid)).getPlayers()[1] == null;
        }
        return ok;
    }

    public static void joinRoom(UUID roomUuid, String loginUUID) {
        var room = RoomService.uidToRoom.get(roomUuid);
        room.addSecond(loginUUID);
        RoomService.uidToRoom.remove(roomUuid);
        RoomService.uidToRoom.put((roomUuid), room);//*/
    }

    public static void leaveRoom(UUID roomUuid, String loginUUID) {
        if (roomUuid == null || loginUUID == null) {
            return;
        }
        final var player0 = RoomService.uidToRoom.get(roomUuid).getPlayers()[0];
        final var player1 = RoomService.uidToRoom.get(roomUuid).getPlayers()[1];
        if (player0 != null && player0.equals(loginUUID)) {
            RoomService.uidToRoom.get(roomUuid).removePlayer(0);
        } else if (player1 != null && player1.equals(loginUUID)) {
            RoomService.uidToRoom.get(roomUuid).removePlayer(1);
        }

        if (RoomService.uidToRoom.get(roomUuid).getPlayers()[0] == null
                && RoomService.uidToRoom.get(roomUuid).getPlayers()[1] == null) {
            RoomService.uidToRoom.remove(roomUuid);
        }

        RoomService.uidToRoom.get(roomUuid).resetNrOfReady();
    }

    public static UUID getRoomUUIDOfPlayer(String loginUUID) {
        for (final var key : RoomService.uidToRoom.keySet()) {
            final var player0 = RoomService.uidToRoom.get(key).getPlayers()[0];
            final var player1 = RoomService.uidToRoom.get(key).getPlayers()[1];
            if (player0 != null && player0.equals(loginUUID)) {
                return key;
            }
            if (player1 != null && player1.equals(loginUUID)) {
                return key;
            }
        }
        return null;
    }

    public static String getOtherPlayer(UUID roomUuid, String loginUUID) {
        if (RoomService.uidToRoom.get(roomUuid).getPlayers()[0].equals(loginUUID)) {
            return RoomService.uidToRoom.get(roomUuid).getPlayers()[1];
        }
        return RoomService.uidToRoom.get(roomUuid).getPlayers()[0];
    }
}
