package Prasunet.company.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        userService.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void shouldSignupUserSuccessfully() {
        // GIVEN
        String email = "test@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // WHEN
        User user = userService.signup("Test User", email, "password123", "STUDENT");

        // THEN
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertTrue(passwordEncoder.matches("password123", user.getPassword()));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionIfEmailAlreadyExists() {
        // GIVEN
        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        // THEN
        assertThrows(RuntimeException.class, () ->
                userService.signup("Test", "test@gmail.com", "pass", "STUDENT")
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        // GIVEN
        User user = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode("password123"))
                .build();

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        // WHEN
        User loggedInUser = userService.login("test@gmail.com", "password123");

        // THEN
        assertNotNull(loggedInUser);
    }

    @Test
    void shouldFailLoginForWrongPassword() {
        // GIVEN
        User user = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode("password123"))
                .build();

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        // THEN
        assertThrows(RuntimeException.class, () ->
                userService.login("test@gmail.com", "wrongpass")
        );
    }
}
