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

    List<CommentaireDTO> getComments(String name, String action) throws CommentaireInexistantException;

    @Transactional
    CommentaireDTO addComment(String name, AddCommentDTO commentaireDTO) throws PasDeContenuException;

    @Transactional
    CommentaireDTO addInteraction(String name, AddInteractionDTO interactionDTO) throws CommentaireInexistantException;

    @Transactional
    void deleteComment(String name, Integer idCommentaire) throws CommentaireInexistantException, AuteurInconnuException;

    @Transactional
    CommentaireDTO editComment(String name, Integer idCommentaire, UpdateCommentaireDTO updateCommentaireDTO) throws PasDeContenuException, CommentaireInexistantException, AuteurInconnuException;
}
