package eyemazev20.Controllers.views;

import eyemazev20.Dtos.http.GameHistoryViewDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@SuppressWarnings("unused")
@SessionAttributes("loginUUID")
@Controller
public class GameViewController {
    @GetMapping("/past-game/{uuid}")
    public String getPastGameStats(
            @PathVariable final UUID uuid,
            final HttpSession httpSession,
            final Model model
    ) throws InterruptedException {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        var loginUUID = httpSession.getAttribute("loginUUID");
        Thread.sleep((new Random()).nextInt(2000 - 500) + 500);

        var pgDto = GameService.getPastGameData(uuid, loginUUID.toString());
        model.addAttribute("pg", pgDto.toJson());
        RoomService.uidToRoom.remove(uuid);
        return "past-game";
    }

    @GetMapping("/history")
    public String getHistoryPage(final HttpSession httpSession, final Model model) {
        if (!AuthService.isAuth(httpSession)) return ("redirect:/");
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var pastGames = GameService.getPastGamesOfUser(loginUUID.toString());

        final var ans = new ArrayList<GameHistoryViewDto>();
        for (final var pastGame : pastGames) {
            GameHistoryViewDto.STATE state;
            int meIdx, otherIdx;

            if (pastGame.getPlUUIDs() != null && loginUUID.toString().equals(pastGame.getPlUUIDs()[0])) {
                meIdx = 0;
            } else {
                meIdx = 1;
            }
            otherIdx = 1 - meIdx;

            if (pastGame.getScores()[meIdx] < pastGame.getScores()[otherIdx]) {
                state = GameHistoryViewDto.STATE.LOST;
            } else if (pastGame.getScores()[meIdx] > pastGame.getScores()[otherIdx]) {
                state = GameHistoryViewDto.STATE.WON;
            } else {
                state = GameHistoryViewDto.STATE.DRAW;
            }

            ans.add(new GameHistoryViewDto(pastGame.getRoomUUID(), state));
        }

        Collections.reverse(ans);
        model.addAttribute("pastGames", ans.toString());
        return "history";
    }
}
