package eyemazev20.Services;

import eyemazev20.Dtos.http.MazeParams;
import eyemazev20.Models.entities.Room;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class RoomService {
    public static HashMap<UUID, Room> uidToRoom = new HashMap<>();

    public static @NotNull UUID createRoom(
            final String loginUUID,
            final MazeParams mazeParams,
            final String mzName,
            final boolean isPublic
    ) {
        final var uuid = UUID.randomUUID();
        RoomService.uidToRoom.put(uuid, new Room(loginUUID, mazeParams, mzName, isPublic));
        System.out.println("UUID:" + uuid);
        System.out.println(RoomService.uidToRoom.get(uuid));
        return uuid;
    }

    public static boolean canBeJoined(UUID roomUuid) {
        boolean ok = false;
        ok |= RoomService.uidToRoom.containsKey((roomUuid));
        if (ok) {
            ok = false;
            ok |= RoomService.uidToRoom.get((roomUuid)).getPlUUIDs()[0] == null;
            ok |= RoomService.uidToRoom.get((roomUuid)).getPlUUIDs()[1] == null;
        }
        return ok;
    }

    public static void joinRoom(UUID roomUuid, String loginUUID) {
        var room = RoomService.uidToRoom.get(roomUuid);
        room.addSecond(loginUUID);
        RoomService.uidToRoom.remove(roomUuid);
        RoomService.uidToRoom.put(roomUuid, room);//*/
    }

    public static void leaveRoom(UUID roomUuid, String loginUUID) {
        if (roomUuid == null || loginUUID == null) {
            return;
        }
        final var player0 = RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[0];
        final var player1 = RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[1];
        if (player0 != null && player0.equals(loginUUID)) {
            RoomService.uidToRoom.get(roomUuid).removePlayer(0);
        } else if (player1 != null && player1.equals(loginUUID)) {
            RoomService.uidToRoom.get(roomUuid).removePlayer(1);
        }

        RoomService.uidToRoom.get(roomUuid).resetNrOfReady();

        if (RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[0] == null
                && RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[1] == null) {
            RoomService.uidToRoom.remove(roomUuid);
        }
    }

    public static @Nullable UUID getRoomUUIDOfPlayer(String loginUUID) {
        for (final var key : RoomService.uidToRoom.keySet()) {
            final var player0 = RoomService.uidToRoom.get(key).getPlUUIDs()[0];
            final var player1 = RoomService.uidToRoom.get(key).getPlUUIDs()[1];
            if (player0 != null && player0.equals(loginUUID)) {
                return key;
            }
            if (player1 != null && player1.equals(loginUUID)) {
                return key;
            }
        }
        return null;
    }

    public static @Nullable String getOtherPlayer(UUID roomUuid, String loginUUID) {
        if (RoomService.uidToRoom.get(roomUuid) == null || RoomService.uidToRoom.get(roomUuid).getPlUUIDs() == null) {
            return null;
        }
        if (
                RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[0] != null &&
                RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[0].equals(loginUUID)
        ) {
            return RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[1];
        }
        return RoomService.uidToRoom.get(roomUuid).getPlUUIDs()[0];
    }

}
