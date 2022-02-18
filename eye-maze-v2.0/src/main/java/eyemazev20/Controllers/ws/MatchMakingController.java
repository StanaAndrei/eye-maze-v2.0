package eyemazev20.Controllers.ws;

import eyemazev20.Services.MatchMakingService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.ws.MatchMkMesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class MatchMakingController {
    @Autowired
    private MatchMkMesService matchMkMesService;

    @MessageMapping("/mmk")
    private void matchMaking(final @NotNull Principal principal) throws InterruptedException {
        MatchMakingService.addToOrRemoveFromSearchers(principal.getName());
        //System.out.println("SOCK_MMK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (MatchMakingService.canExtract()) {
            final var pair = MatchMakingService.extract();

            matchMkMesService.sendMmkMes(pair.getValue(), "");

            UUID roomUUID;
            do {
                roomUUID = RoomService.getRoomUUIDOfPlayer(pair.getValue());
            } while (roomUUID == null);

            matchMkMesService.sendMmkMes(pair.getKey(), roomUUID.toString());//*/
        }
    }
}
