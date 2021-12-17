package eyemazev20.controllers;

import eyemazev20.Dtos.AuthReq;
import eyemazev20.Dtos.RegReq;
import eyemazev20.Services.AuthService;
import eyemazev20.utils.UtilVars;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequestMapping("/api")
@RestController
public class AuthController {
    Set<String> currAuth = new HashSet<>();
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
            return new ResponseEntity<>(LOGIN_STATS.WRONG_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (currAuth.contains(loginUUID.toString())) {
            return new ResponseEntity<>(LOGIN_STATS.ALREADY_AUTH, HttpStatus.IM_USED);
        }

        currAuth.add(loginUUID.toString());
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
        currAuth.remove(httpSession.getAttribute("loginUUID").toString());
        httpSession.removeAttribute("loginUUID");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
