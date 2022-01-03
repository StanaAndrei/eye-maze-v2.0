package eyemazev20.controllers.views.lobby;

import eyemazev20.Services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@SessionAttributes("loginUUID")
@Controller
public class ArenaViewController {
    @GetMapping("/arena/{uuid}")
    public ModelAndView getArenaPage(@PathVariable UUID uuid, HttpSession httpSession) {
        if (httpSession.getAttribute("loginUUID") == null) {
            return new ModelAndView("redirect:/login");
        }

        if (!RoomService.uidToRoom.containsKey(uuid)) {
            return new ModelAndView("redirect:/");
        }

        final  var loginUUID = httpSession.getAttribute("loginUUID").toString();

        final var player0 = RoomService.uidToRoom.get(uuid).getPlUUIDs()[0];
        final var player1 = RoomService.uidToRoom.get(uuid).getPlUUIDs()[1];

        if (player0 == null && player1 == null) {
            RoomService.uidToRoom.remove(uuid);
            return new ModelAndView("redirect:/play");
        }
        if (loginUUID != null) {
            if (player0 != null && !player0.equals(loginUUID) && player1 != null && !player1.equals(loginUUID)) {
                return new ModelAndView("redirect:/play");
            }
        }//*/
        final var mav = new ModelAndView("arena");
        return mav;
    }
}
