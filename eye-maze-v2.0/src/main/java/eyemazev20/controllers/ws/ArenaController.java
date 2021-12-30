package eyemazev20.controllers.ws;

import eyemazev20.Dtos.StringDto;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.ws.EntityCtrlService;
import eyemazev20.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.util.UUID;

@Controller
public class ArenaController {
    @Autowired
    private EntityCtrlService entityCtrlService;

    @MessageMapping("/move-message")
    @SendToUser("/topic/move-message")
    @MessageExceptionHandler(MessageConversionException.class)
    private StringDto movePl(final StringDto moveData, final Principal principal) {
        StringDto gameState;//return game state as str json
        final UUID roomUUID = RoomService.getRoomUUIDOfPlayer(principal.getName());
        final var other = RoomService.getOtherPlayer(roomUUID, principal.getName());
        if (moveData.getBuffer().isEmpty()) {
            gameState = new StringDto(RoomService.uidToRoom.get(roomUUID).game.getGameStateAsJson(roomUUID).toString());
            entityCtrlService.sendMoveMessage(other, gameState);
            return gameState;
        }
        final var plUUIDs = RoomService.uidToRoom.get(roomUUID).getPlUUIDs();
        final int playerNr = principal.getName().equals(plUUIDs[0]) ? 0 : 1;
        final boolean validMove = GameService.movePlayer(roomUUID, moveData.getBuffer(), playerNr);
        if (!validMove) {
            return null;
        }
        gameState = new StringDto(RoomService.uidToRoom.get(roomUUID).game.getGameStateAsJson(roomUUID).toString());

        boolean isGameOver = true;
        for (final var player : RoomService.uidToRoom.get(roomUUID).game.getPlayers()) {
            isGameOver &= player.hadFinished();
        }

        if (isGameOver) {
            gameState.setBuffer("OVER");
            GameService.addGameToPastGames(roomUUID);
        }

        entityCtrlService.sendMoveMessage(other, gameState);
        return gameState;
    }
}
