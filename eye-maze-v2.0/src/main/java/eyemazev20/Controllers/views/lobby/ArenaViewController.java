package eyemazev20.Controllers.views.lobby;

import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.Random;
import java.util.UUID;

@SessionAttributes("loginUUID")
@Controller
public class ArenaViewController {
    @GetMapping("/arena/{uuid}")
    public String getArenaPage(@PathVariable final UUID uuid, final HttpSession httpSession, final Model model) throws InterruptedException {
        if (httpSession.getAttribute("loginUUID") == null) {
            return ("redirect:/login");
        }

        if (!RoomService.uidToRoom.containsKey(uuid)) {
            return ("redirect:/");
        }

        final  var loginUUID = httpSession.getAttribute("loginUUID").toString();

        final var player0 = RoomService.uidToRoom.get(uuid).getPlUUIDs()[0];
        final var player1 = RoomService.uidToRoom.get(uuid).getPlUUIDs()[1];

        if (player0 == null && player1 == null) {
            RoomService.uidToRoom.remove(uuid);
            return ("redirect:/play");
        }
        if (loginUUID != null) {
            if (player0 != null && !player0.equals(loginUUID) && player1 != null && !player1.equals(loginUUID)) {
                return ("redirect:/play");
            }
        }

        String myLoginUUID, otherLoginUUID;
        if (player0.equals(loginUUID)) {
            myLoginUUID = player0;
            otherLoginUUID = player1;
        } else {
            myLoginUUID = player1;
            otherLoginUUID = player0;
        }

        Thread.sleep((new Random()).nextInt(1000));
        final var user = UserService.getUserData(myLoginUUID);
        model.addAttribute("myProfilePicB64", user.getProfilePicB64());
        Thread.sleep((new Random()).nextInt(1000));
        final var other = UserService.getUserData(otherLoginUUID);
        model.addAttribute("otherProfilePicB64", other.getProfilePicB64());
        return "arena";
    }
}
