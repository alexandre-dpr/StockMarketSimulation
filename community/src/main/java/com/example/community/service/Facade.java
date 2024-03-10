package com.example.community.service;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.AuteurNonReconnueException;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;
import jakarta.transaction.Transactional;

import java.util.List;

public interface Facade {

    List<CommentaireDTO> getAllCommentaire(String action) throws CommentaireInexistantException;

    @Transactional
    CommentaireDTO addComentaire(AddCommentDTO commentaireDTO);

    @Transactional
    CommentaireDTO addInteraction(String userId, Integer idCommentaire) throws CommentaireInexistantException;

    @Transactional
    void deleteCommentaire(String userId, Integer idCommentaire) throws CommentaireInexistantException, AuteurNonReconnueException;
}
