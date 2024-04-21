package community.service;

import community.dto.request.AddCommentDTO;
import community.dto.request.AddInteractionDTO;
import community.dto.request.UpdateCommentaireDTO;
import community.dto.response.CommentaireDTO;
import community.exceptions.AuteurInconnuException;
import community.exceptions.CommentaireInexistantException;
import community.exceptions.PasDeContenuException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ICommunityService {
    /**
     * Retourne la liste des commentaires pour l'action donnée
     *
     * @param action l'action pour laquelle on veut les commentaires
     * @return la liste des commentaires pour l'action donnée
     * @throws CommentaireInexistantException si aucun commentaire n'est trouvé
     */
    List<CommentaireDTO> getComments(String action) throws CommentaireInexistantException;

    /**
     * Ajoute un commentaire pour l'action donnée
     *
     * @param name           le nom de l'auteur du commentaire
     * @param commentaireDTO les informations du commentaire à ajouter
     * @return le commentaire ajouté
     * @throws PasDeContenuException si le commentaire est vide
     */
    @Transactional
    CommentaireDTO addComment(String name, AddCommentDTO commentaireDTO) throws PasDeContenuException;

    /**
     * Ajoute une interaction à un commentaire
     *
     * @param name           le nom de l'auteur de l'interaction
     * @param interactionDTO les informations de l'interaction à ajouter
     * @return le commentaire modifié
     * @throws CommentaireInexistantException si le commentaire n'existe pas
     */
    @Transactional
    CommentaireDTO addInteraction(String name, AddInteractionDTO interactionDTO) throws CommentaireInexistantException;

    /**
     * Supprime un commentaire
     *
     * @param name          le nom de celui qui veut supprimer le commentaire
     * @param idCommentaire l'identifiant du commentaire à supprimer
     * @throws CommentaireInexistantException si le commentaire n'existe pas
     * @throws AuteurInconnuException         si l'auteur du commentaire n'est pas le bon
     */
    @Transactional
    void deleteComment(String name, Integer idCommentaire) throws CommentaireInexistantException, AuteurInconnuException;

    /**
     * Modifie un commentaire
     *
     * @param name                 le nom de l'auteur du commentaire
     * @param idCommentaire        l'identifiant du commentaire à modifier
     * @param updateCommentaireDTO les informations du commentaire à modifier
     * @return le commentaire modifié
     * @throws PasDeContenuException          si le commentaire est vide
     * @throws CommentaireInexistantException si le commentaire n'existe pas
     * @throws AuteurInconnuException         si l'auteur du commentaire n'est pas le bon
     */
    @Transactional
    CommentaireDTO editComment(String name, Integer idCommentaire, UpdateCommentaireDTO updateCommentaireDTO) throws PasDeContenuException, CommentaireInexistantException, AuteurInconnuException;
}
