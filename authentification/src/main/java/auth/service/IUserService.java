package auth.service;

import auth.exceptions.BadLoginException;
import auth.exceptions.ExistingUserException;
import jakarta.transaction.Transactional;

public interface IUserService {
    @Transactional
    String register(String email, String username, String password) throws ExistingUserException;

    String login(String login, String password) throws BadLoginException;
}
