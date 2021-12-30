package eyemazev20.controllers;

import eyemazev20.Dtos.PastGameDto;
import eyemazev20.Dtos.StringDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import org.springframework.boot.Banner;
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
        //get ready state
        final var roomUUID = RoomService.getRoomUUIDOfPlayer(loginUUID);

        return modelAndView;
    }

    @GetMapping("/arena/{uuid}")
    public ModelAndView getArenaPage(@PathVariable UUID uuid, HttpSession httpSession) {
        if (httpSession.getAttribute("loginUUID") == null) {
            return new ModelAndView("redirect:/login");
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

    @GetMapping("/rooms")
    public ModelAndView getLobbies(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ModelAndView("redirect:/");
        }
        final var mav = new ModelAndView("rooms");
        //System.out.println(RoomService.uidToRoom.keySet());

        mav.addObject("codes", RoomService.uidToRoom.keySet());
        return mav;
    }

    @GetMapping("/mk-room")
    public String getMkRoomPg(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) return "redirect:/";
        return "mkroom";
    }

    @GetMapping("/past-game/{uuid}")
    public ModelAndView getPastGameStats(@PathVariable UUID uuid, HttpSession httpSession) {
        final var mav = new ModelAndView("past-game");
        StringDto pastgameData = new StringDto();
        final var pastGame = GameService.getPastGameData(uuid);

        final String pl0 = UserService.getUserData(pastGame.getPlUUIDs()[0]).getUsername();
        final String pl1 = UserService.getUserData(pastGame.getPlUUIDs()[1]).getUsername();

        PastGameDto pastGameDto = new PastGameDto(new String[]{pl0, pl1}, pastGame.getScores());
        mav.addObject("pg", pastGameDto.toJson());
        return mav;
    }
}