package eyemazev20.Controllers.api;

import eyemazev20.Dtos.http.UserInfoDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.UserService;
import eyemazev20.Models.orm.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api")
public class UserController {
    @PutMapping("/change/{currPassword}")
    public ResponseEntity<?> changeUserAtr(
            final HttpSession httpSession,
            @RequestBody final UserInfoDto userInfoDto,
            @PathVariable final String currPassword
    ) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var user = UserService.getUserData(loginUUID.toString());
        if (!user.getPassword().equals(currPassword)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserService.updateUserInfo(user.getId(), userInfoDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user-data")
    public ResponseEntity<User> fetchUserData(final HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var user = UserService.getUserData(loginUUID.toString());
        System.out.println("HEREEEEEEE");
        return ResponseEntity.ok(user);
    }
}
