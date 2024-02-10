package com.example.community.service;

import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;
import com.example.community.model.Interaction;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class FacadeImpl implements Facade{

    private static FacadeImpl facade;
    private ArrayList<Commentaire> commentaires;

    private EntityManager e;

    private FacadeImpl() {
        this.commentaires = new ArrayList<>();
    }

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
    public ArrayList<Commentaire> getAllCommentaire() {
        return commentaires;
    }

    @Override
    public Commentaire addComentaire(Integer userId, String content) {
        Commentaire commentaire = new Commentaire(userId, content);
        commentaires.add(commentaire);
        return commentaire;
    }

    @Override
    public Commentaire addInteraction(Integer userId, Integer idCommentaire) throws CommentaireInexistantException {
        Commentaire commentaire = this.getCommentaireById(idCommentaire);
        commentaire.getInteractions().put(userId, Interaction.LIKE);
        return commentaire;
    }

    @Override
    public void deleteCommentaire(Integer idCommentaire) throws CommentaireInexistantException {
        commentaires.remove(this.getCommentaireById(idCommentaire));
    }

    private Commentaire getCommentaireById(Integer idCommentaire) throws CommentaireInexistantException{
        for (Commentaire c : commentaires){
            if (idCommentaire.equals(c.getId())){
                return c;
            }
        }
        throw new CommentaireInexistantException("Comment not found");
    }
}
