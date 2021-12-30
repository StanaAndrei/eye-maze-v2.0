package eyemazev20.controllers;

import eyemazev20.Services.AuthService;
import eyemazev20.Services.RoomService;
import eyemazev20.models.Room;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api")
public class MatchController {
    @PostMapping("/create-room")
    public ResponseEntity<UUID> createRoom(HttpSession httpSession) throws IOException {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        leaveRoom(httpSession);
        final var uuid = UUID.randomUUID();
        final var loginUUID = httpSession.getAttribute("loginUUID").toString();
        RoomService.uidToRoom.put(uuid, new Room(loginUUID));
        httpSession.setAttribute("currRoomUUID", uuid.toString());
        return ResponseEntity.ok(uuid);
    }

    @PutMapping("/join-room/{roomUuid}")
    public ResponseEntity<Void> joinRoom(@PathVariable UUID roomUuid, HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!RoomService.canBeJoined(roomUuid)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        leaveRoom(httpSession);
        httpSession.setAttribute("currRoomUUID", roomUuid.toString());
        RoomService.joinRoom(roomUuid, httpSession.getAttribute("loginUUID").toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/leave-room")
    public ResponseEntity<Void> leaveRoom(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final var loginUUID = httpSession.getAttribute("loginUUID").toString();
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(loginUUID);
        RoomService.leaveRoom(roomUUID, loginUUID);
        httpSession.removeAttribute("currRoomUUID");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getAllRooms(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(RoomService.uidToRoom.keySet());
    }
}
