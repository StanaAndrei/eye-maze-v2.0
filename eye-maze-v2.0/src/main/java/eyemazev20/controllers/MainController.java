package eyemazev20.controllers;

import eyemazev20.Services.AuthService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@SessionAttributes("loginUUID")
@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(HttpSession httpSession) {
        return "index";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession httpSession) {
        return "register";
    }

    @GetMapping("/login")
    public ModelAndView loginPage(HttpSession httpSession) {
        final var mav = new ModelAndView("login");
        return mav;
    }

    @GetMapping("/user")
    public ModelAndView userPage(HttpSession httpSession) {
        var modelAndView = new ModelAndView("user");
        final var attr = httpSession.getAttribute("loginUUID");
        if (attr == null) {
            return new ModelAndView("redirect:/login");
        }
        final var user = UserService.getUserData(attr.toString());
        modelAndView.addObject("username", user.getUsername());//*/
        return modelAndView;
    }

    @GetMapping("/play")
    public String getPlayPage(HttpSession httpSession) {
        if (httpSession.getAttribute("loginUUID") == null) {
            return "redirect:/login";
        }

        return "play";
    }

    @GetMapping("/room/{uuid}")
    public ModelAndView getRoomPage(@PathVariable UUID uuid, HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ModelAndView("redirect:/login");
        }
        final  var loginUUID = httpSession.getAttribute("loginUUID").toString();

        final var player0 = RoomService.uidToRoom.get(uuid).players[0];
        final var player1 = RoomService.uidToRoom.get(uuid).players[1];

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
}