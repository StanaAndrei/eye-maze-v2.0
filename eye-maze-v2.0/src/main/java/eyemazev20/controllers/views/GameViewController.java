package eyemazev20.controllers.views;

import eyemazev20.Dtos.http.PastGameDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import eyemazev20.models.orm.PastGame;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;

@SuppressWarnings("unused")
@SessionAttributes("loginUUID")
@Controller
public class GameViewController {
    @GetMapping("/past-game/{uuid}")
    public String getPastGameStats(@PathVariable final UUID uuid, final HttpSession httpSession, final Model model) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        var loginUUID = httpSession.getAttribute("loginUUID");
        var pgDto = GameService.getPastGameData(uuid, loginUUID.toString());
        model.addAttribute("pg", pgDto.toJson());
        RoomService.uidToRoom.remove(uuid);
        return "past-game";
    }

    @GetMapping("/history")
    public ModelAndView getHistoryPage(final HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) return new ModelAndView("redirect:/");
        final var mav = new ModelAndView("history");
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var res = GameService.getPastGamesOfUser(loginUUID.toString());

        for (var pastgame : res) {
            if (!pastgame.getPlUUIDs()[0].equals(loginUUID.toString())) {
                {
                    var aux = pastgame.getPlUUIDs()[0];
                    pastgame.getPlUUIDs()[0] = pastgame.getPlUUIDs()[1];
                    pastgame.getPlUUIDs()[1] = aux;
                }
                {
                    var aux = pastgame.getScores()[0];
                    pastgame.getScores()[0] = pastgame.getScores()[1];
                    pastgame.getScores()[1] = aux;
                }
            }
        }
        Collections.reverse(res);
        mav.addObject("pastGames", res);
        return mav;
    }
}
