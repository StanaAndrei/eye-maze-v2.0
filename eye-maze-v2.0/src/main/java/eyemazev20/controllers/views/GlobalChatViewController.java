package eyemazev20.controllers.views;

import eyemazev20.Dtos.http.GlobalMessageOutDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.GlobalMessageService;
import eyemazev20.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Controller
public class GlobalChatViewController {
    @GetMapping("/global-chat")
    public String getGlobalChView(HttpSession httpSession, Model model) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var messages = GlobalMessageService.getMessages();
        final var list = new ArrayList<GlobalMessageOutDto>();
        for (final var message : messages) {
            final var user = UserService.getUserData(message.getSenderId());
            list.add(new GlobalMessageOutDto(
                        message.getContent(),
                        message.getTimestp(),
                        user.getProfilePicB64(),
                        user.getUsername(),
                        user.getLoginUUID()
                    )
            );
        }
        model.addAttribute("messages", list);
        return "global-chat";
    }
}
