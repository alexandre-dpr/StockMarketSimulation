package community.service;

import community.dto.request.AddCommentDTO;
import community.dto.request.AddInteractionDTO;
import community.dto.request.UpdateCommentaireDTO;
import community.dto.response.CommentaireDTO;
import community.exceptions.AuteurInconnuException;
import community.exceptions.CommentaireInexistantException;
import community.exceptions.PasDeContenuException;
import community.model.Commentaire;
import community.repository.CommentaireRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("CommunityService")
public class CommunityService implements ICommunityService {

    @Autowired
    CommentaireRepository commentaireRepository;

    @Override
    public List<CommentaireDTO> getComments(String action) {
        Optional<List<Commentaire>> commentaires = commentaireRepository.findAllByActionOrderByDateDesc(action);
        if (commentaires.isEmpty()) {
            return new ArrayList<>();
        }
        List<CommentaireDTO> commentaireDTOS = new ArrayList<>();
        commentaires.get().forEach(e -> commentaireDTOS.add(e.toDTO()));
        return commentaireDTOS;
    }

    @Override
    @Transactional
    public CommentaireDTO addComment(String name, AddCommentDTO commentaireDTO) throws PasDeContenuException {
        if (commentaireDTO.content().length() == 0) {
            throw new PasDeContenuException("Le commentaire est vide.");
        }
        Commentaire commentaire = new Commentaire(name, commentaireDTO.content(), commentaireDTO.ticker());
        commentaireRepository.save(commentaire);
        return commentaire.toDTO();
    }

    @Override
    @Transactional
    public CommentaireDTO addInteraction(String user, AddInteractionDTO interactionDTO) throws CommentaireInexistantException {
        Optional<Commentaire> commentaire = commentaireRepository.findById(interactionDTO.idComment());
        if (commentaire.isEmpty()) {
            throw new CommentaireInexistantException("Commentaire non trouvée");
        }
        commentaire.get().addInteraction(user, interactionDTO.interaction());
        commentaireRepository.save(commentaire.get());
        return commentaire.get().toDTO();
    }

    @Transactional
    @Override
    public void deleteComment(String username, Integer idCommentaire) throws CommentaireInexistantException, AuteurInconnuException {

        Optional<Commentaire> commentaire = commentaireRepository.findById(idCommentaire);

        if (commentaire.isEmpty()) {
            throw new CommentaireInexistantException("Commentaire non trouvée");
        }
        Commentaire comm = commentaire.get();
        if (!username.equals(comm.getUser())) {
            throw new AuteurInconnuException("Seul l'auteur du commentaire peut le supprimer");
        }
        commentaireRepository.delete(commentaire.get());
    }

    @Override
    public CommentaireDTO editComment(String name, Integer idCommentaire, UpdateCommentaireDTO updateCommentaireDTO) throws PasDeContenuException, CommentaireInexistantException, AuteurInconnuException {
        Optional<Commentaire> commentaire = commentaireRepository.findById(idCommentaire);

        if (commentaire.isEmpty()) {
            throw new CommentaireInexistantException("Commentaire non trouvée");
        }
        if (!(commentaire.get().getUser().equals(name))) {
            throw new AuteurInconnuException("Seul l'auteur du commentaire peut le modifier");
        }
        if (updateCommentaireDTO.content().length() == 0) {
            throw new PasDeContenuException("Le commentaire est vide.");
        }
        commentaire.get().setContent(updateCommentaireDTO.content());
        commentaireRepository.save(commentaire.get());
        return commentaire.get().toDTO();
    }

}
