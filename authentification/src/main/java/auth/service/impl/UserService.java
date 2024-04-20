package auth.service.impl;

import auth.exceptions.BadLoginException;
import auth.exceptions.ExistingUserException;
import auth.modele.User;
import auth.repository.UserRepository;
import auth.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;


@Service("UserService")
public class UserService implements IUserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Function<User, String> generateToken;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public String register(String email, String username, String password) throws ExistingUserException {
        Optional<User> user = userRepository.findByEmailOrUsername(email, username);

        if (user.isPresent()) {
            throw new ExistingUserException("User already exists");
        }

        String pwHashed = passwordEncoder.encode(password);
        User addedUser = new User(email, username, pwHashed);

        userRepository.save(addedUser);
        return generateToken.apply(addedUser);
    }

    @Override
    public String login(String login, String password) throws BadLoginException {
        Optional<User> user = userRepository.findByEmailOrUsername(login, login);
        if (user.isEmpty()) {
            throw new BadLoginException("No user found.");
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadLoginException("Incorrect password.");
        }

        return generateToken.apply(user.get());
    }
}
