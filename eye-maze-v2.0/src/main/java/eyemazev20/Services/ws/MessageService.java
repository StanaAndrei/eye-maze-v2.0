package eyemazev20.Services.ws;

import eyemazev20.Dtos.ws.ResMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
}
