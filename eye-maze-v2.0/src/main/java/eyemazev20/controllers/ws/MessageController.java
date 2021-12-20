package eyemazev20.controllers.ws;

import eyemazev20.Dtos.ws.LaunchResMess;
import eyemazev20.Dtos.ws.Message;
import eyemazev20.Dtos.ws.ResMessage;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import eyemazev20.Services.ws.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

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
        LaunchResMess launchResMess = new LaunchResMess(user.getUsername(), state);//*/
        if (other == null) {
            return launchResMess;
        }
        messageService.sendLauchMess(other, launchResMess);

        if (RoomService.uidToRoom.get(roomUUID).canLaunch()) {
            Thread.sleep(1500);
            state = LaunchResMess.State.LAUNCH;
            launchResMess = new LaunchResMess(user.getUsername(), state);
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
