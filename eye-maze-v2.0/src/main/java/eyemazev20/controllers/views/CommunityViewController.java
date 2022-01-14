package eyemazev20.controllers.views;

import eyemazev20.Services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommunityViewController {
    @GetMapping("/community")
    public String getComView(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        return "community";
    }
}
