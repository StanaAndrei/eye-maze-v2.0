package eyemazev20.Controllers.views;

import eyemazev20.Dtos.http.GlobalCustomMzDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.MazeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class CommunityViewController {
    @GetMapping("/community")
    public String getComView(final HttpSession httpSession, final Model model) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        final var rows = MazeServices.getAllMazes();
        List<GlobalCustomMzDto> mazes = new ArrayList<>();
        for (final var row : rows) {
            mazes.add(new GlobalCustomMzDto(
                    (int) row[0],
                    (String) row[1],
                    (String) row[2]
            ));
        }
        Collections.reverse(mazes);
        model.addAttribute("mazes", mazes);
        return "community";
    }
}
