package eyemazev20.controllers;

import eyemazev20.Dtos.LoginUUIDDto;
import eyemazev20.Services.UserService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SessionAttributes("loginUUID")
@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView mainPage() {
        var modelAndView = new ModelAndView("index");
        modelAndView.addObject("name", "world");
        return modelAndView;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession httpSession) {
        return "login";
    }

    @GetMapping("/user")
    public ModelAndView userPage(HttpSession httpSession) {
        var modelAndView = new ModelAndView("user");
        final var attr = httpSession.getAttribute("loginUUID");
        System.out.println(attr);
        if (attr == null) {
            System.err.println("ATR--->NULL");
            return new ModelAndView("redirect:/login");
        }
        final var user = UserService.getUserData(attr.toString());
        modelAndView.addObject("username", user.getUsername());//*/
        return modelAndView;
    }
}