package com.example.community.service;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.AuteurNonReconnueException;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;
import com.example.community.repository.CommentaireRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("CommunityService")
public class FacadeImpl implements Facade{

    @Autowired
    CommentaireRepository commentaireRepository;

    @Override
    public List<CommentaireDTO> getAllCommentaire(String action) {
        Optional <List<Commentaire>> commentaires = commentaireRepository.findAllByActionOrderByDateDesc(action);
        if(commentaires.isEmpty()) {
            return new ArrayList<>();
        }
        List<CommentaireDTO> commentaireDTOS = new ArrayList<>();
        commentaires.get().forEach(e -> {
            commentaireDTOS.add(e.toDTO());
        });
        return commentaireDTOS;
    }

    @Override
    @Transactional
    public CommentaireDTO addComentaire(String name, AddCommentDTO commentaireDTO,String id) {
        Commentaire commentaire = new Commentaire(name, commentaireDTO.content(), id);
        commentaireRepository.save(commentaire);
        return commentaire.toDTO();
    }

    @Override
    @Transactional
    public CommentaireDTO addInteraction(String userId, Integer idCommentaire) throws CommentaireInexistantException {
        Optional<Commentaire> commentaire = commentaireRepository.findById(idCommentaire);
        if(commentaire.isEmpty()) {
            throw new CommentaireInexistantException("Commentaire non trouvée");
        }
        commentaire.get().addInteraction(userId);
        commentaireRepository.save(commentaire.get());
        return commentaire.get().toDTO();
    }

    @Transactional
    @Override
    public void deleteCommentaire(String username, Integer idCommentaire) throws CommentaireInexistantException, AuteurNonReconnueException {

        Optional<Commentaire> commentaire = commentaireRepository.findById(idCommentaire);

        if(commentaire.isEmpty()) {
            throw new CommentaireInexistantException("Commentaire non trouvée");
        }
        Commentaire comm = commentaire.get();
        if(!username.equals(comm.getUser())){
            throw new AuteurNonReconnueException("Seul l'auteur du commentaire peut le supprimer");
        }
        commentaireRepository.delete(commentaire.get());
    }

}
