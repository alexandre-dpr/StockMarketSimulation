package auth.service;

import auth.exceptions.BadLoginException;
import auth.exceptions.ExistingUserException;
import auth.modele.User;
import auth.repository.UserRepository;
import auth.service.impl.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Function<User, String> generateToken;

    @InjectMocks
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void register_OK() throws ExistingUserException {
        String email = "test@example.com";
        String username = "testuser";
        String password = "password";

        when(userRepository.findByEmailOrUsername(email, username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");
        when(generateToken.apply(any(User.class))).thenReturn("generatedToken");

        String token = userService.register(email, username, password);

        assertNotNull(token);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test(expected = ExistingUserException.class)
    public void register_KO_ExistingUserException() throws ExistingUserException {
        String email = "test@example.com";
        String username = "testuser";
        String password = "password";

        when(userRepository.findByEmailOrUsername(email, username)).thenReturn(Optional.of(new User()));
        userService.register(email, username, password);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void login_OK() throws BadLoginException {
        String login = "test@example.com";
        String password = "password";
        User user = new User();
        user.setPassword("hashedPassword");

        when(userRepository.findByEmailOrUsername(login, login)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(true);
        when(generateToken.apply(any(User.class))).thenReturn("generatedToken");

        String token = userService.login(login, password);

        assertNotNull(token);
    }

    @Test(expected = BadLoginException.class)
    public void login_KO_BadLoginException() throws BadLoginException {
        String login = "test@example.com";
        String password = "password";
        User user = new User();
        user.setPassword("hashedPassword");

        when(userRepository.findByEmailOrUsername(login, login)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "hashedPassword")).thenReturn(false);
        userService.login(login, password);
    }

    @Test(expected = BadLoginException.class)
    public void login_KO_BadLoginException2() throws BadLoginException {
        String login = "test@example.com";
        String password = "password";

        when(userRepository.findByEmailOrUsername(login, login)).thenReturn(Optional.empty());
        userService.login(login, password);
    }

}