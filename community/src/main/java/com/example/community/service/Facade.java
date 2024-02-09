package com.example.community.service;

import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;

import java.util.ArrayList;

public interface Facade {

    ArrayList<Commentaire> getAllCommentaire();

    Commentaire addComentaire(Integer userId, String content);

    Commentaire addInteraction(Integer userId, Integer idCommentaire) throws CommentaireInexistantException;

    void deleteCommentaire(Integer idCommentaire) throws CommentaireInexistantException;
}
