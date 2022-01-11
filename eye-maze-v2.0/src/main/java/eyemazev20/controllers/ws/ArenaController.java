package eyemazev20.controllers.ws;

import eyemazev20.Dtos.http.StringDto;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.ws.EntityCtrlService;
import eyemazev20.models.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.UUID;

@Controller
public class ArenaController {
    private static boolean fiTime = true;

    @Autowired
    private EntityCtrlService entityCtrlService;

    @MessageMapping("/move-message")
    @SendToUser("/topic/move-message")
    @MessageExceptionHandler(MessageConversionException.class)
    private StringDto movePl(final StringDto moveData, final Principal principal) {
        StringDto gameState;//return game state as str json
        final UUID roomUUID = RoomService.getRoomUUIDOfPlayer(principal.getName());
        final var other = RoomService.getOtherPlayer(roomUUID, principal.getName());
        if (other == null) {
            return null;
        }
        if (fiTime) {
            fiTime = false;
            gameState = new StringDto(RoomService.uidToRoom.get(roomUUID).game.getGameStateAsJson(roomUUID).toString());
            entityCtrlService.sendMoveMessage(other, gameState);
            return gameState;
        }
        final var plUUIDs = RoomService.uidToRoom.get(roomUUID).getPlUUIDs();
        final int playerNr = principal.getName().equals(plUUIDs[0]) ? 0 : 1;

        final boolean validMove = GameService.movePlayer(roomUUID, moveData.getBuffer(), playerNr);
        if (!validMove && !RoomService.uidToRoom.get(roomUUID).game.getPlayers()[playerNr].hadFinished()) {
            RoomService.uidToRoom.get(roomUUID).game.getPlayers()[playerNr].nrInv++;
            if (RoomService.uidToRoom.get(roomUUID).game.getPlayers()[playerNr].nrInv == Player.MAX_INV || moveData.getBuffer().equals("afk")) {
                //System.err.println("AFK" + playerNr);
                RoomService.uidToRoom.get(roomUUID).game.getPlayers()[playerNr].mkLoser();
                gameState = new StringDto();
                GameService.finishGame(gameState, roomUUID);//*/
                entityCtrlService.sendMoveMessage(other, gameState);
                return gameState;
            }
            return null;
        } else {
            RoomService.uidToRoom.get(roomUUID).game.getPlayers()[playerNr].nrInv = 0;
        }

        gameState = new StringDto(RoomService.uidToRoom.get(roomUUID).game.getGameStateAsJson(roomUUID).toString());
        boolean isGameOver = true;
        for (final var player : RoomService.uidToRoom.get(roomUUID).game.getPlayers()) {
            isGameOver &= player.hadFinished();
        }
        if (isGameOver) {
            GameService.finishGame(gameState, roomUUID);
        }
        entityCtrlService.sendMoveMessage(other, gameState);
        return gameState;
    }

    @SuppressWarnings("unused")
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        final UUID roomUUID = RoomService.getRoomUUIDOfPlayer(event.getUser().toString());
        if (RoomService.uidToRoom.get(roomUUID) == null) {
            return;
        }
        final var plUUIDs = RoomService.uidToRoom.get(roomUUID).getPlUUIDs();
        final int playerNr = event.getUser().getName().equals(plUUIDs[0]) ? 0 : 1;
        final var other = RoomService.getOtherPlayer(roomUUID, event.getUser().getName());
        if (RoomService.uidToRoom.get(roomUUID).game != null) {
            if (RoomService.uidToRoom.get(roomUUID).disco[playerNr]) {
                System.err.println("Client with username {} disconnected " + event.getUser());
                //System.err.println("AFK" + playerNr);
                RoomService.uidToRoom.get(roomUUID).game.getPlayers()[playerNr].mkLoser();
                var gameState = new StringDto();
                GameService.finishGame(gameState, roomUUID);//*/
                entityCtrlService.sendMoveMessage(other, gameState);
            }
            if (RoomService.uidToRoom.get(roomUUID) != null) {
                RoomService.uidToRoom.get(roomUUID).disco[playerNr] = true;
            }
        }
    }
}
