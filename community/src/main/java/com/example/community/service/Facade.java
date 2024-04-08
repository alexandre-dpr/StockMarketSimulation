package com.example.community.service;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.request.AddInteractionDTO;
import com.example.community.dto.request.UpdateCommentaireDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.AuteurNonReconnueException;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.exceptions.ContentEmptyException;
import com.example.community.model.Commentaire;
import jakarta.transaction.Transactional;

import java.util.List;

public interface Facade {

    List<CommentaireDTO> getAllCommentaire(String name,String action) throws CommentaireInexistantException;

    @Transactional
    CommentaireDTO addComentaire(String name, AddCommentDTO commentaireDTO) throws ContentEmptyException;

    @Transactional
    CommentaireDTO addInteraction(String name, AddInteractionDTO interactionDTO) throws CommentaireInexistantException;

    @Transactional
    void deleteCommentaire(String name, Integer idCommentaire) throws CommentaireInexistantException, AuteurNonReconnueException;

    @Transactional
    CommentaireDTO editCommentaire(String name, Integer idCommentaire, UpdateCommentaireDTO updateCommentaireDTO) throws ContentEmptyException,CommentaireInexistantException, AuteurNonReconnueException;
}
