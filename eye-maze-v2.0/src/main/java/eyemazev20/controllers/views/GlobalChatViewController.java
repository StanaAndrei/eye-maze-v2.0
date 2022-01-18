package eyemazev20.controllers.views;

import eyemazev20.Dtos.http.GlobalMessageOutDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.GlobalMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Controller
public class GlobalChatViewController {
    @GetMapping("/global-chat")
    public String getGlobalChView(final HttpSession httpSession, final Model model) {
        if (!AuthService.isAuth(httpSession)) {
            return "redirect:/login";
        }
        final var rows = GlobalMessageService.getMessages();
        List<GlobalMessageOutDto> messages = new ArrayList<>();
        for (final var row : rows) {
            messages.add(new GlobalMessageOutDto(
                    (String) row[0],
                    (Timestamp) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4]
            ));
        }
        model.addAttribute("messages", messages);//*/
        return "global-chat";
    }
}
