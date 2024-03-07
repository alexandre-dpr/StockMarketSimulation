package com.example.community.service;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.response.CommentaireDTO;
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

    private static FacadeImpl facade;

    @Autowired
    CommentaireRepository commentaireRepository;

    /**
     * @return singeleton facade
     */
    public static FacadeImpl getFacade() {
        if (facade == null) {
            facade = new FacadeImpl();
        }
        return facade;
    }
    @Override
    public List<CommentaireDTO> getAllCommentaire(String action) throws CommentaireInexistantException {
        Optional <List<Commentaire>> commentaires = commentaireRepository.findAllByActionOrderByDateDesc(action);
        if(commentaires.isEmpty()) {
            throw new CommentaireInexistantException("Comment not found ");
        }
        List<CommentaireDTO> commentaireDTOS = new ArrayList<>();
        commentaires.get().forEach(e -> {
            commentaireDTOS.add(e.toDTO());
        });
        return commentaireDTOS;
    }

    @Override
    @Transactional
    public CommentaireDTO addComentaire(AddCommentDTO commentaireDTO) {
        Commentaire commentaire = new Commentaire(commentaireDTO.userId(), commentaireDTO.content(), commentaireDTO.action());
        commentaireRepository.save(commentaire);
        return commentaire.toDTO();
    }

    @Override
    @Transactional
    public CommentaireDTO addInteraction(String userId, Integer idCommentaire) throws CommentaireInexistantException {
        Optional<Commentaire> commentaire = commentaireRepository.findById(idCommentaire);
        if(commentaire.isEmpty()) {
            throw new CommentaireInexistantException("Comment not found ");
        }
        commentaire.get().addInteraction(userId);
        commentaireRepository.save(commentaire.get());
        return commentaire.get().toDTO();
    }

    @Transactional
    @Override
    public void deleteCommentaire(Integer idCommentaire) throws CommentaireInexistantException {
        Optional<Commentaire> commentaire = commentaireRepository.findById(idCommentaire);
        if(commentaire.isEmpty()) {
            throw new CommentaireInexistantException("Comment not found ");
        }
        commentaireRepository.delete(commentaire.get());
    }

}
