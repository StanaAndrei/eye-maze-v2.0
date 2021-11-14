package eyemazev20.controllers;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView mainPage() {
        var modelAndView = new ModelAndView("index");
        modelAndView.addObject("name", "jon");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }
}