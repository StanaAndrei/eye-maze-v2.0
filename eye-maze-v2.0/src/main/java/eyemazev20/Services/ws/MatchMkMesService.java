package eyemazev20.Services.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MatchMkMesService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public MatchMkMesService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMmkMes(final String userId, final String roomUUID) {
        simpMessagingTemplate.convertAndSendToUser(userId, "/topic/mmk", roomUUID);
    }
}
