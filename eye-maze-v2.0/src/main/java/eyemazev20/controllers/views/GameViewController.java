package eyemazev20.controllers.views;

import eyemazev20.Dtos.http.PastGameDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.UUID;

@SessionAttributes("loginUUID")
@Controller
public class GameViewController {
    @GetMapping("/past-game/{uuid}")
    public ModelAndView getPastGameStats(@PathVariable UUID uuid, HttpSession httpSession) {
        final var mav = new ModelAndView("past-game");
        try {
            final var pastGame = GameService.getPastGameData(uuid);
            //Thread.sleep((long) (1000 * Math.random() + Math.random() * 100));
            final String pl0 = UserService.getUserData(pastGame.getPlUUIDs()[0]).getUsername();
            //Thread.sleep((long) (1000 * Math.random() + Math.random() * 100));
            final String pl1 = UserService.getUserData(pastGame.getPlUUIDs()[1]).getUsername();
            //Thread.sleep((long) (1000 * Math.random() + Math.random() * 100));

            //System.out.println("tst:" + pastGame.getTimestp());
            var pastGameDto = new PastGameDto(new String[]{pl0, pl1}, pastGame.getScores(), pastGame.getTimestp());
            mav.addObject("pg", pastGameDto.toJson());
            RoomService.uidToRoom.remove(uuid);
            httpSession.removeAttribute("currRoomUUID");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            return mav;
        }
    }

    @GetMapping("/history")
    public ModelAndView getHistoryPage(HttpSession httpSession) {
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
