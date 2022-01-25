package eyemazev20.controllers.views.lobby;

import eyemazev20.Services.AuthService;
import eyemazev20.Services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SessionAttributes("loginUUID")
@Controller
public class RoomViewController {

    @GetMapping("/room/{uuid}")
    public ModelAndView getRoomPage(@PathVariable UUID uuid, HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ModelAndView("redirect:/login");
        }
        if (RoomService.uidToRoom.get(uuid) == null) {
            return (new ModelAndView("redirect:/play"));
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
        }

        final var modelAndView = new ModelAndView("room");
        modelAndView.addObject("roomCode", uuid);
        return modelAndView;
    }

    @GetMapping("/rooms")
    public ModelAndView getLobbies(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ModelAndView("redirect:/login");
        }
        final var mav = new ModelAndView("rooms");

        List<String> list = new ArrayList<>();
        for (final var code : RoomService.uidToRoom.keySet()) {
            if (RoomService.canBeJoined(code)) {
                list.add(code.toString());
            }
        }

        mav.addObject("codes", list);
        return mav;
    }

    @GetMapping("/mkroom")
    public String getMkRoomPg(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }


        return "mkroom";
    }

    @GetMapping("/play")
    public String getPlayPage(HttpSession httpSession) {
        if (httpSession.getAttribute("loginUUID") == null) {
            return "redirect:/login";
        }
        final var loginUUID = httpSession.getAttribute("loginUUID").toString();
        for (var key : RoomService.uidToRoom.keySet()) {/*
            if (Arrays.stream(RoomService.uidToRoom.get(key).getPlUUIDs()).anyMatch(e -> e == null || (e != null && e.equals(loginUUID)))) {
                httpSession.removeAttribute("currRoomUUID");
            }//*/
        }


        return "play";
    }
}
