package eyemazev20.Controllers.api;

import eyemazev20.Dtos.http.MazeParams;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.MazeServices;
import eyemazev20.Services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api")
public class MatchController {
    @PostMapping("/create-room")
    public ResponseEntity<UUID> createRoom(
            final HttpSession httpSession,
            @RequestBody(required = false) final MazeParams mazeParams,
            @RequestParam Map<String, String> queryParams
    ) {
        if (!queryParams.containsKey("is-public") || !queryParams.containsKey("mz-name")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final var isPublic = Boolean.parseBoolean(queryParams.get("is-public"));

        var mzName = queryParams.get("mz-name");
        if (mzName.length() == 0) {
            mzName = null;
        }

        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (mzName != null && !MazeServices.canAccessMz(mzName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //leaveRoom(httpSession);

        final var loginUUID = httpSession.getAttribute("loginUUID").toString();
        final var roomUUID = RoomService.createRoom(loginUUID, mazeParams, mzName, isPublic);

        return ResponseEntity.ok(roomUUID);//*/
        //return null;
    }

    @PutMapping("/join-room/{roomUuid}")
    public ResponseEntity<Void> joinRoom(@PathVariable final UUID roomUuid, final HttpSession httpSession) {
        //System.err.println("JOIN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (RoomService.uidToRoom.get(roomUuid) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!RoomService.canBeJoined(roomUuid)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        leaveRoom(httpSession);
        //httpSession.setAttribute("currRoomUUID", roomUuid.toString());
        RoomService.joinRoom(roomUuid, httpSession.getAttribute("loginUUID").toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/leave-room")
    public ResponseEntity<Void> leaveRoom(final HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final var loginUUID = httpSession.getAttribute("loginUUID").toString();
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(loginUUID);
        RoomService.leaveRoom(roomUUID, loginUUID);
        //httpSession.removeAttribute("currRoomUUID");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
