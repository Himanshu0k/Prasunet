package Prasunet.company.User;

import Prasunet.company.Dtos.LoginRequest;
import Prasunet.company.Dtos.SignupRequest;
import Prasunet.company.JWT.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // ============================
    // SIGNUP
    // ============================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {

        User user = userService.signup(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "role", user.getRole().name()
                )
        );
    }

    // ============================
    // LOGIN
    // ============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "role", user.getRole().name()
                )
        );
    }
}
