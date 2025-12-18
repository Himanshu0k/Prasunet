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
        String email = "test@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = userService.signup("Test User", email, "password123", Role.STUDENT);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertTrue(passwordEncoder.matches("password123", user.getPassword()));
    }


    @Test
    void shouldThrowExceptionIfEmailAlreadyExists() {
        // GIVEN
        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        // THEN
        assertThrows(RuntimeException.class, () ->
                userService.signup("Test", "test@gmail.com", "pass", Role.STUDENT)
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

    @Test
    void shouldThrowExceptionForEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                userService.signup("Name", "", "password123", Role.STUDENT)
        );
    }

    @Test
    void shouldThrowExceptionForInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                userService.signup("Name", "invalid-email", "password123", Role.STUDENT)
        );
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundOnLogin() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.login("unknown@test.com", "password123")
        );
    }

}
