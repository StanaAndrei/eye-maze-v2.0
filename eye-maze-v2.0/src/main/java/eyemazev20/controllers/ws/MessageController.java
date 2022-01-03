package eyemazev20.controllers.ws;

import eyemazev20.Dtos.ws.LaunchResMess;
import eyemazev20.Dtos.ws.Message;
import eyemazev20.Dtos.ws.ResMessage;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import eyemazev20.Services.ws.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import java.security.Principal;
import java.util.UUID;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    private static void launch(final UUID roomUUID) throws InterruptedException {
        System.out.println("LAUNCH!!!!" + roomUUID.toString());
        GameService.initGame(roomUUID);
    }

    @MessageMapping("/launch-message")
    @SendToUser("/topic/launch-message")
    public LaunchResMess launchGame(final Principal principal) throws InterruptedException {
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(principal.getName());

        if (!RoomService.uidToRoom.get(roomUUID).addToReady(principal.getName())) {
            return null;
        }

        final var user = UserService.getUserData(principal.getName());
        final var other = RoomService.getOtherPlayer(roomUUID, principal.getName());
        var state = LaunchResMess.State.READY;
        //state = LaunchResMess.State.LAUNCH;//just for the debug
        LaunchResMess launchResMess = new LaunchResMess(user.getUsername(), state);//*/
        if (other == null) {
            return launchResMess;
        }
        messageService.sendLauchMess(other, launchResMess);


        if (RoomService.uidToRoom.get(roomUUID).canLaunch()) {
            state = LaunchResMess.State.LAUNCH;
            launchResMess = new LaunchResMess(user.getUsername(), state);
            MessageController.launch(roomUUID);
            messageService.sendLauchMess(other, launchResMess);
        }

        return launchResMess;
    }

    @MessageMapping("/room-messages")
    @SendToUser("/topic/room-messages")
    public ResMessage sendRoomMessage(final Message message, final Principal principal) {
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(principal.getName());
        final var target = RoomService.getOtherPlayer(roomUUID, principal.getName());

        if (target == null) {
            return new ResMessage(message.getMessageContent(), "you");
        }

        final var user = UserService.getUserData(principal.getName());

        messageService.sendPrivMess(target, message.getMessageContent(), user.getUsername());//*/
        return new ResMessage(message.getMessageContent(), "you");
    }
}
