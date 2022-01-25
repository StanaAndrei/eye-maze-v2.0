package eyemazev20.Services.ws;

import eyemazev20.Dtos.ws.JoinLeaveDto;
import eyemazev20.Dtos.ws.LaunchResMess;
import eyemazev20.Dtos.ws.ResMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MessageService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendPrivMess(String userId, String message, String from) {
        final var resMessage = new ResMessage(message, from);
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/room-messages", resMessage);
    }

    public void sendLauchMess(String userId, LaunchResMess launchResMess) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/launch-message", launchResMess);
    }

    public void sendJoinLeaveMessage(String userId, JoinLeaveDto.STATE state, String who) {
        if (userId == null) {
            //System.err.println("NULL");
            return;
        }

        final var joinLeaveDto = new JoinLeaveDto(who, state);
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/join-leave", joinLeaveDto);
        //System.out.println("SEND");
    }
}
