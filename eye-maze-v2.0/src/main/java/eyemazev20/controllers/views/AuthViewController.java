package eyemazev20.controllers.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@SessionAttributes("loginUUID")
@Controller
public class AuthViewController {
    @GetMapping("/register")
    public String registerPage(HttpSession httpSession) {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession httpSession) {
        return "login";
    }
}
