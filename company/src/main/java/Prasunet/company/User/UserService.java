package Prasunet.company.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // For testing
    void setPasswordEncoder(BCryptPasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }

    public User signup(String name, String email, String password, Role role) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.STUDENT)
                .createdAt(Instant.now())
                .build();

        return userRepository.save(user);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }
}
