package eyemazev20.controllers.ws;

import eyemazev20.Dtos.http.StringDto;
import eyemazev20.Dtos.ws.JoinLeaveDto;
import eyemazev20.Dtos.ws.LaunchResMess;
import eyemazev20.Dtos.ws.ResMessage;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import eyemazev20.Services.ws.MessageService;
import eyemazev20.models.entities.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.server.HandshakeRequest;
import java.security.Principal;

@SuppressWarnings("unused")
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @EventListener
    public void onApplicationEvent(final SessionSubscribeEvent event) {
        final String destination = SimpMessageHeaderAccessor.wrap(event.getMessage()).getDestination();
        if (!destination.equals("/user/topic/join-leave")) {
            return;
        }

        final var curr = event.getUser().getName();
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(curr);
        final var other = RoomService.getOtherPlayer(roomUUID, curr);

        final var currUser = UserService.getUserData(curr);//*/

        messageService.sendJoinLeaveMessage(other, JoinLeaveDto.STATE.JOINED, currUser.getUsername());
        messageService.sendJoinLeaveMessage(curr, JoinLeaveDto.STATE.JOINED, currUser.getUsername());

        if (!RoomService.canBeJoined(roomUUID)) {
            final var otherUser = UserService.getUserData(other);
            messageService.sendJoinLeaveMessage(curr, JoinLeaveDto.STATE.JOINED, otherUser.getUsername());
        }
    }

    @EventListener
    private void handleSessionDisconnect(final SessionDisconnectEvent event) {

        final var curr = event.getUser().getName();
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(curr);
        final var other = RoomService.getOtherPlayer(roomUUID, curr);

        final var currUser = UserService.getUserData(curr);

        messageService.sendJoinLeaveMessage(other, JoinLeaveDto.STATE.LEFT, currUser.getUsername());
        if (RoomService.uidToRoom.get(roomUUID) != null && RoomService.uidToRoom.get(roomUUID).game == null) {
            RoomService.leaveRoom(roomUUID, curr);
        }//*/
    }

    @MessageMapping("/launch-message")
    @SendToUser("/topic/launch-message")
    public LaunchResMess launchGame(final Principal principal) throws Exception {
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
            GameService.initGame(roomUUID);
            messageService.sendLauchMess(other, launchResMess);
        }

        return launchResMess;
    }

    @MessageMapping("/room-messages")
    @SendToUser("/topic/room-messages")
    public ResMessage sendRoomMessage(final StringDto message, final Principal principal) {
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(principal.getName());
        final var target = RoomService.getOtherPlayer(roomUUID, principal.getName());

        final String buffer = HtmlUtils.htmlEscape(message.getBuffer());
        if (target == null) {
            return new ResMessage(buffer, "you");
        }

        final var user = UserService.getUserData(principal.getName());

        messageService.sendPrivMess(target, buffer, user.getUsername());//*/
        return new ResMessage(buffer, "you");
    }
}
