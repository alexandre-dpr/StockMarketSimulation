package auth.service;

import auth.exceptions.BadLoginException;
import auth.exceptions.ExistingUserException;
import jakarta.transaction.Transactional;

public interface IUserService {
    /**
     * Enregistre un nouvel utilisateur
     *
     * @param email    email de l'utilisateur
     * @param username nom d'utilisateur
     * @param password mot de passe
     * @return le token de l'utilisateur
     * @throws ExistingUserException si l'utilisateur existe déjà
     */
    @Transactional
    String register(String email, String username, String password) throws ExistingUserException;

    /**
     * Connecte un utilisateur
     *
     * @param login    nom d'utilisateur ou email
     * @param password mot de passe
     * @return le token de l'utilisateur
     * @throws BadLoginException si le login ou le mot de passe est incorrect
     */
    String login(String login, String password) throws BadLoginException;
}
