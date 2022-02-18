package eyemazev20.Controllers.ws;

import eyemazev20.Dtos.http.StringDto;
import eyemazev20.Dtos.ws.JoinLeaveDto;
import eyemazev20.Dtos.ws.LaunchResMess;
import eyemazev20.Dtos.ws.ResMessage;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import eyemazev20.Services.ws.MessageService;
import eyemazev20.Models.orm.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.Random;

@SuppressWarnings("unused")
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @EventListener
    public void onApplicationEvent(final SessionSubscribeEvent event) throws InterruptedException {
        final String destination = SimpMessageHeaderAccessor.wrap(event.getMessage()).getDestination();
        if (!destination.equals("/user/topic/join-leave")) {
            return;
        }

        //System.out.println("JOINNNNNNNNNNN");
        final var curr = event.getUser().getName();
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(curr);
        final var other = RoomService.getOtherPlayer(roomUUID, curr);

        Thread.sleep((new Random()).nextInt(1000 - 300) + 300);
        final var currUser = UserService.getUserData(curr);//*/

        messageService.sendJoinLeaveMessage(other, JoinLeaveDto.STATE.JOINED, currUser.getUsername());
        messageService.sendJoinLeaveMessage(curr, JoinLeaveDto.STATE.JOINED, currUser.getUsername());

        //System.err.println("---------------______________----------------------------------------");
        if (!RoomService.canBeJoined(roomUUID)) {
            Thread.sleep((new Random()).nextInt(1000 - 300) + 300);
            User otherUser = null;
            try {
                otherUser = UserService.getUserData(other);
            } catch (Exception e) {
                return;
            }
            messageService.sendJoinLeaveMessage(curr, JoinLeaveDto.STATE.JOINED, otherUser.getUsername());
        }
    }

    @EventListener
    private void handleSessionDisconnect(final SessionDisconnectEvent event) throws InterruptedException {
        //System.err.println("---------------______________----------------------------------------");

        final var curr = event.getUser().getName();

        for (final var room : RoomService.uidToRoom.values()) {
            if (
                    (room.getPlUUIDs()[0] != null && room.getPlUUIDs()[0].equals(curr)) ||
                    (room.getPlUUIDs()[1] != null && room.getPlUUIDs()[1].equals(curr))
            ) {
                //System.err.println("---------------______________----------------------------------------" + curr);
                final var roomUUID = RoomService.getRoomUUIDOfPlayer(curr);
                final var other = RoomService.getOtherPlayer(roomUUID, curr);

                Thread.sleep((new Random()).nextInt(1500 - 500) + 500);
                final var currUser = UserService.getUserData(curr);

                messageService.sendJoinLeaveMessage(other, JoinLeaveDto.STATE.LEFT, currUser.getUsername());
                if (RoomService.uidToRoom.get(roomUUID) != null && RoomService.uidToRoom.get(roomUUID).game == null) {
                    RoomService.leaveRoom(roomUUID, curr);
                }//*/
                return;
            }
        }
    }

    @MessageMapping("/launch-message")
    @SendToUser("/topic/launch-message")
    public LaunchResMess launchGame(final Principal principal) throws Exception {
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(principal.getName());
        final var user = UserService.getUserData(principal.getName());
        final var other = RoomService.getOtherPlayer(roomUUID, principal.getName());
        var state = LaunchResMess.State.READY;
        if (
                other != null &&
                RoomService.uidToRoom.get(roomUUID) != null &&
                !RoomService.uidToRoom.get(roomUUID).addToReady(principal.getName())
        ) {
            return null;
        }


        //state = LaunchResMess.State.LAUNCH;//just for the debug
        LaunchResMess launchResMess = new LaunchResMess(user.getUsername(), state);//*/
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
