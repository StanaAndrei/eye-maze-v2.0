package eyemazev20.controllers;

import eyemazev20.Dtos.PastGameDto;
import eyemazev20.Dtos.StringDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.GameService;
import eyemazev20.Services.RoomService;
import eyemazev20.Services.UserService;
import eyemazev20.models.Game;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;

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

        mav.addObject("codes", RoomService.uidToRoom.keySet());
        return mav;
    }

    @GetMapping("/mk-room")
    public String getMkRoomPg(HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) return "redirect:/";
        return "mkroom";
    }

    @GetMapping("/past-game/{uuid}")
    public ModelAndView getPastGameStats(@PathVariable UUID uuid) {
        final var mav = new ModelAndView("past-game");
        try {
            final var pastGame = GameService.getPastGameData(uuid);
            Thread.sleep((long) (1000 * Math.random() + Math.random() * 100));
            final String pl0 = UserService.getUserData(pastGame.getPlUUIDs()[0]).getUsername();
            Thread.sleep((long) (1000 * Math.random() + Math.random() * 100));
            final String pl1 = UserService.getUserData(pastGame.getPlUUIDs()[1]).getUsername();
            Thread.sleep((long) (1000 * Math.random() + Math.random() * 100));

            PastGameDto pastGameDto = new PastGameDto(new String[]{pl0, pl1}, pastGame.getScores());
            mav.addObject("pg", pastGameDto.toJson());
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