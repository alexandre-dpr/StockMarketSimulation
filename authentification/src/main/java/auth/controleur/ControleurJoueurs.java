package auth.controleur;

import auth.dto.LoginDto;
import auth.dto.UserDto;
import auth.service.UserService;
import auth.service.exceptions.BadLoginException;
import auth.service.exceptions.ExistingUserException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/auth")
@EnableWebSecurity
public class ControleurJoueurs {

    @Autowired
    UserService userService;

    @PostMapping(value = "/register")
    public ResponseEntity<String> inscription(@Valid @RequestBody UserDto userDto) {
        try {
            String token = userService.register(userDto.getEmail(), userDto.getUsername(), userDto.getPassword());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{pseudo}")
                    .buildAndExpand(userDto.getUsername()).toUri();

            return ResponseEntity.created(location).header(HttpHeaders.AUTHORIZATION, token).build();

        } catch (ExistingUserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            String token = userService.login(loginDto.getLogin(), loginDto.getPassword());
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).build();

        } catch (BadLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
