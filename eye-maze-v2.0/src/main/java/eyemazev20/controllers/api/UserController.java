package eyemazev20.controllers.api;

import eyemazev20.Dtos.http.UserInfoUpDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class UserController {
    @PutMapping("/change/{currPassword}")
    public ResponseEntity<?> changeUserAtr(
            final HttpSession httpSession,
            @RequestBody final UserInfoUpDto userInfoUpDto,
            @PathVariable final String currPassword
    ) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.err.println(currPassword);
        System.err.println(userInfoUpDto);
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var user = UserService.getUserData(loginUUID.toString());
        if (!user.getPassword().equals(currPassword)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserService.updateUserInfo(user.getId(), userInfoUpDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
