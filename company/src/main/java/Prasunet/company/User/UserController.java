package Prasunet.company.User;

import Prasunet.company.Dtos.LoginRequest;
import Prasunet.company.Dtos.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(
                userService.signup(
                        request.getName(),
                        request.getEmail(),
                        request.getPassword(),
                        request.getRole()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                userService.login(request.getEmail(), request.getPassword())
        );
    }
}

