package eyemazev20.Services.ws;

import eyemazev20.Dtos.StringDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class EntityCtrlService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public EntityCtrlService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMoveMessage(String userId, StringDto moveData) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/move-message", moveData);
    }
}
