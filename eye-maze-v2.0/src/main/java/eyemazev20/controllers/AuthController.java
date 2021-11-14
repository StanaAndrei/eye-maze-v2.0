package eyemazev20.controllers;

import eyemazev20.Dtos.AuthReq;
import eyemazev20.Dtos.RegReq;
import eyemazev20.Services.AuthService;
import eyemazev20.utils.UtilVars;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api")
@RestController
public class AuthController {

    @PostMapping("/login")
    private ResponseEntity<UUID> login(@RequestBody AuthReq authReq) {
        final var loginUUID = AuthService.getLoginUUID(authReq);
        return ResponseEntity.ok(loginUUID);
    }

    @PostMapping("/register")
    private ResponseEntity<Integer> register(@RequestBody @Validated RegReq regReq) {
        final int id = AuthService.confirmReg(regReq);
        return ResponseEntity.ok(id);
    }
}
