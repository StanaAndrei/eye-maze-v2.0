package eyemazev20.controllers.views;

import eyemazev20.Services.AuthService;
import eyemazev20.Services.MazeServices;
import eyemazev20.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class MazeViewController {
    @GetMapping(value = {"/mkmaze", "/mkmaze/{mzname}"})
    public String mkMaze(
            final HttpSession httpSession,
            @PathVariable(required = false, name = "mzname") final String mzname,
            final Model model
    ) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        if (mzname != null) {
            try {
                final var mzData = MazeServices.getDataByName(mzname);
                if (mzData != null) {
                    model.addAttribute("mzform", mzData.getForm());
                }
            } catch (Exception e) {
                //System.err.println("ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
                return "mkmaze";
            }
        }//*/
        if (mzname == null) {
            model.addAttribute("mzform", "TO_BE_CREATED");
        }
        return "mkmaze";
    }

    @GetMapping("/my-mazes")
    public String viewAllMazes(final HttpSession httpSession, final Model model) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var user = UserService.getUserData(loginUUID.toString());
        final var mazesName = MazeServices.getAllMazesName(user.getId());
        model.addAttribute("names", mazesName);
        return "my-mazes";
    }

    @GetMapping("/random-made-mazes")
    public String viewAllGenMazes(final HttpSession httpSession, final Model model) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        final var res = MazeServices.getAllPcGenMazes();
        model.addAttribute("mazes", res);
        return "random-made-mazes";
    }
}
