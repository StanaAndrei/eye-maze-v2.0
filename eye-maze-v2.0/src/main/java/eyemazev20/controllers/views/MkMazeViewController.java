package eyemazev20.controllers.views;

import eyemazev20.Services.AuthService;
import eyemazev20.Services.MazeServices;
import eyemazev20.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MkMazeViewController {
    @GetMapping("/mkmaze")
    public String mkMaze(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        return "mkmaze";
    }

    @GetMapping("/my-mazes")
    public String viewAllMazes(HttpSession httpSession, Model model) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var user = UserService.getUserData(loginUUID.toString());
        final var mazesName = MazeServices.getAllMazesName(user.getId());
        model.addAttribute("names", mazesName);
        return "my-mazes";
    }
}
