package eyemazev20.Controllers.ws;

import eyemazev20.Dtos.http.GlobalMessageOutDto;
import eyemazev20.Dtos.http.StringDto;
import eyemazev20.Services.GlobalMessageService;
import eyemazev20.Services.UserService;
import eyemazev20.Models.orm.GlobalMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.sql.Timestamp;

@Controller
public class GlobalMessageController {
    @MessageMapping("/global-messages")
    @SendTo("/topic/global-messages")
    public String sendGlobalmessage(final StringDto message, final Principal principal, StompHeaderAccessor headers) {
        final var user = UserService.getUserData(principal.getName());
        final var filteredMessage = HtmlUtils.htmlEscape(message.getBuffer());
        final var timestamp = new Timestamp(System.currentTimeMillis());
        final var globalMessage = new GlobalMessage(filteredMessage, timestamp, user.getId());
        GlobalMessageService.addToDb(globalMessage);
        return (new GlobalMessageOutDto(
                filteredMessage,
                globalMessage.getTimestp(),
                user.getProfilePicB64(),
                user.getUsername(),
                principal.getName()
        )).toString();
    }

}
