package eyemazev20.Controllers.api;

import eyemazev20.Dtos.http.MazeFormDto;
import eyemazev20.Services.AuthService;
import eyemazev20.Services.MazeGenService;
import eyemazev20.Services.MazeServices;
import eyemazev20.Services.UserService;
import eyemazev20.Models.entities.MazeForm;
import eyemazev20.Models.orm.MazeOrm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class MazeController {
    @PostMapping("/upload-maze")
    public ResponseEntity<Void> uploadMaze(
            @RequestBody MazeFormDto mazeFormDto,
            final HttpSession httpSession,
            @RequestParam(required = false, name = "update", defaultValue = "false") final boolean update
    ) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (mazeFormDto.getName().isEmpty() || mazeFormDto.getStart() == null || mazeFormDto.getFinish() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final var loginUUID = httpSession.getAttribute("loginUUID");
        final var user = UserService.getUserData(loginUUID.toString());
        if (!MazeForm.isValid(mazeFormDto)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        MazeServices.saveMazeToDb(new MazeOrm(mazeFormDto.getName(), mazeFormDto.toString(), user.getId()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove-maze/{mzname}")
    public ResponseEntity<?> removeMazeFromDb(@PathVariable final String mzname, final HttpSession httpSession) {
        if (!AuthService.isAuth(httpSession)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final int mazeId = MazeServices.getDataByName(mzname).getId();
        MazeServices.removeMazeFromDb(mazeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/rand-maze-way")//just for testing
    public ResponseEntity<?> getRandMaze(@RequestParam int n, @RequestParam int m) {
        if (n == 0 || m == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final var mazeWay = MazeGenService.genMaze(n, m);
        return ResponseEntity.ok(mazeWay);
    }
}
