package eyemazev20.controllers.views;

import eyemazev20.Services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@SuppressWarnings("unused")
@Controller
public class GlobalChatViewController {
    @GetMapping("/global-chat")
    public String getGlobalChView(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        return "global-chat";
    }
}
