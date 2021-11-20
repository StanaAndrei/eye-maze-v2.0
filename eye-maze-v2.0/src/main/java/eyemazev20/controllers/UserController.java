package eyemazev20.controllers;

import eyemazev20.Dtos.UsernamePutReq;
import eyemazev20.Services.AuthService;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.User;
import eyemazev20.utils.UtilVars;
import netscape.javascript.JSObject;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @PutMapping("/change/{atr}")
    public ResponseEntity<Boolean> changeUserAtr() {
        return null;//tbc
    }
}
