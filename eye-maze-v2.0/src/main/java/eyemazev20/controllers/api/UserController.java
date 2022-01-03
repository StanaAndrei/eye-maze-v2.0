package eyemazev20.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
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
