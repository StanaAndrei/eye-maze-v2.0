package eyemazev20.Controllers.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SessionAttributes("loginUUID")
@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}