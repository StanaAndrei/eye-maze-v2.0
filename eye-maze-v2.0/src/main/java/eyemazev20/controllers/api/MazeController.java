package eyemazev20.controllers.api;

import eyemazev20.Services.MazeGenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MazeController {

    @GetMapping("/rand-maze-way")
    public static ResponseEntity<?> getRandMaze(@RequestParam int n, @RequestParam int m) {
        if (n == 0 || m == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final var mazeWay = MazeGenService.genMaze(n, m);
        return ResponseEntity.ok(mazeWay);
    }
}
