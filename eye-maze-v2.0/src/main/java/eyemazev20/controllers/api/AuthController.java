package eyemazev20.controllers.api;

import eyemazev20.Dtos.http.AuthReq;
import eyemazev20.Dtos.http.RegReq;
import eyemazev20.Services.AuthService;
import eyemazev20.config.HttpSessionConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequestMapping("/api")
@RestController
public class AuthController {
    enum LOGIN_STATS {
        ALREADY_AUTH,
        SUCCESS,
        WRONG_DATA,
    }

    @PostMapping("/login")
    private ResponseEntity<LOGIN_STATS> login(@RequestBody AuthReq authReq, HttpSession httpSession) {
        UUID loginUUID;
        try {
            loginUUID = AuthService.getLoginUUID(authReq);
        } catch (Exception e) {
            return new ResponseEntity<>(LOGIN_STATS.WRONG_DATA, HttpStatus.CONFLICT);
        }

        if (httpSession.getAttribute("loginUUID") != null) {
            return new ResponseEntity<>(LOGIN_STATS.ALREADY_AUTH, HttpStatus.IM_USED);
        }

        httpSession.setAttribute("loginUUID", loginUUID);
        return ResponseEntity.ok(LOGIN_STATS.SUCCESS);
    }

    @PostMapping("/register")
    private ResponseEntity<Integer> register(@RequestBody @Validated RegReq regReq) {
        final int id = AuthService.confirmReg(regReq);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/logout")
    private ResponseEntity<Void> logout(HttpSession httpSession) {
        if (httpSession.getAttribute("loginUUID") == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        httpSession.removeAttribute("loginUUID");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
