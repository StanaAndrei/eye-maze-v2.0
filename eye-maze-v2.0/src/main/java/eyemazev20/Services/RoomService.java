package eyemazev20.Services;

import eyemazev20.models.Room;
import eyemazev20.utils.RoomsHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class RoomService {
    //public static HashMap<UUID, Room> uidToRoom = new HashMap<>();

    public static boolean canBeJoined(UUID roomUuid) {
        boolean confl = false;
        confl |= !RoomsHolder.uidToRoom.containsKey((roomUuid));
        if (!confl) {
            confl |= RoomsHolder.uidToRoom.get((roomUuid)).getPlayers()[1] != null;
        }
        return confl;
    }

    public static void joinRoom(UUID roomUuid, String loginUUID) {
        var room = RoomsHolder.uidToRoom.get(roomUuid);
        room.addSecond(loginUUID);
        RoomsHolder.uidToRoom.remove(roomUuid);
        RoomsHolder.uidToRoom.put((roomUuid), room);//*/
    }

    public static void leaveRoom(UUID roomUuid, String loginUUID) {
        if (RoomsHolder.uidToRoom.get(roomUuid).getPlayers()[0].equals(loginUUID)) {
            RoomsHolder.uidToRoom.get(roomUuid).players[0] = null;
        } else if (RoomsHolder.uidToRoom.get(roomUuid).getPlayers()[1].equals(loginUUID)) {
            RoomsHolder.uidToRoom.get(roomUuid).players[1] = null;
        }
    }

    public static UUID getRoomUUIDOfPlayer(String loginUUID) {
        for (final var key : RoomsHolder.uidToRoom.keySet()) {
            if (RoomsHolder.uidToRoom.get(key).players[0].equals(loginUUID)) {
                return key;
            }
            if (RoomsHolder.uidToRoom.get(key).players[1].equals(loginUUID)) {
                return key;
            }
        }
        return null;
    }

    public static String getOtherPlayer(UUID roomUuid, String loginUUID) {
        if (RoomsHolder.uidToRoom.get(roomUuid).getPlayers()[0].equals(loginUUID)) {
            return RoomsHolder.uidToRoom.get(roomUuid).players[1];
        }
        return RoomsHolder.uidToRoom.get(roomUuid).players[0];
    }
}
