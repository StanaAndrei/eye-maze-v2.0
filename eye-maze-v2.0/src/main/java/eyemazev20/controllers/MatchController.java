package eyemazev20.controllers;

import eyemazev20.Services.AuthService;
import eyemazev20.Services.RoomService;
import eyemazev20.models.Room;
import eyemazev20.utils.RoomsHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MatchController {
    @PostMapping("/create-room")
    public ResponseEntity<UUID> createRoom(HttpSession httpSession) throws IOException {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final var uuid = UUID.randomUUID();
        final var loginUUID = httpSession.getAttribute("loginUUID").toString();
        RoomsHolder.uidToRoom.put(uuid, new Room(loginUUID));
        return ResponseEntity.ok(uuid);
    }

    @PutMapping("/join-room/{roomUuid}")
    public ResponseEntity<?> joinRoom(@PathVariable UUID roomUuid, HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (RoomService.canBeJoined(roomUuid)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        RoomService.joinRoom(roomUuid, httpSession.getAttribute("loginUUID").toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/leave-room/{roomUuid}")
    public ResponseEntity<?> leaveRoom(@PathVariable UUID roomUuid, HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        RoomService.leaveRoom(roomUuid, httpSession.getAttribute("loginUUID").toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
