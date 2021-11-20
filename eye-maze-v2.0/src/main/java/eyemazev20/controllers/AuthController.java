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
import java.util.List;
import java.util.UUID;

@RequestMapping("/api")
@RestController
public class AuthController {

    @PostMapping("/login")
    private ResponseEntity<UUID> login(@RequestBody AuthReq authReq, HttpSession httpSession) {
        final var loginUUID = AuthService.getLoginUUID(authReq);
        httpSession.setAttribute("loginUUID", loginUUID);
        return ResponseEntity.ok(loginUUID);
    }

    @PostMapping("/register")
    private ResponseEntity<Integer> register(@RequestBody @Validated RegReq regReq) {
        final int id = AuthService.confirmReg(regReq);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/logout")
    private ResponseEntity<Void> logout(HttpSession httpSession) {
        System.err.println(httpSession.getAttribute("loginUUID"));
        if (httpSession.getAttribute("loginUUID") == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        httpSession.removeAttribute("loginUUID");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
