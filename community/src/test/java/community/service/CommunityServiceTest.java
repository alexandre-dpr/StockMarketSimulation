package community.service;

import community.dto.request.AddCommentDTO;
import community.dto.request.AddInteractionDTO;
import community.dto.request.UpdateCommentaireDTO;
import community.dto.response.CommentaireDTO;
import community.exceptions.AuteurInconnuException;
import community.exceptions.CommentaireInexistantException;
import community.exceptions.PasDeContenuException;
import community.model.Commentaire;
import community.model.Interaction;
import community.repository.CommentaireRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class CommunityServiceTest {

    @Mock
    private CommentaireRepository commentaireRepository;

    @InjectMocks
    private CommunityService facade;


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCommentaire_OK() {
        String action = "AAPL";
        List<Commentaire> commentaireList = new ArrayList<>();
        List<CommentaireDTO> commentaireDTOList = new ArrayList<>();
        commentaireList.add(new Commentaire("user", "blabla", action));
        commentaireList.add(new Commentaire("user", "blabla2", action));
        commentaireList.forEach(e -> {
            commentaireDTOList.add(e.toDTO());
        });
        when(commentaireRepository.findAllByActionOrderByDateDesc(action)).thenReturn(Optional.of(commentaireList));
        List<CommentaireDTO> result = facade.getComments(action);
        assertEquals(commentaireDTOList, result);
    }

    @Test
    public void getAllCommentaire_Empty_OK() {
        String action = "someAction";
        when(commentaireRepository.findAllByActionOrderByDateDesc(action)).thenReturn(Optional.empty());
        List<CommentaireDTO> result = facade.getComments(action);
        assertEquals(0, result.size());
    }

    @Test
    public void addComentaire_OK() throws PasDeContenuException {
        String name = "user";
        String content = "Ceci est un commentaire";
        String ticker = "AAPL";
        AddCommentDTO commentaireDTO = new AddCommentDTO(content, ticker);
        Commentaire commentaire = new Commentaire(name, commentaireDTO.content(), commentaireDTO.ticker());
        when(commentaireRepository.save(commentaire)).thenReturn(commentaire);
        CommentaireDTO result = facade.addComment(name, commentaireDTO);
        assertNotNull(result);
        assertEquals(commentaire.toDTO(), result);
    }

    @Test(expected = PasDeContenuException.class)
    public void addComentaire_KO_ContentEmptyException() throws PasDeContenuException {
        String name = "user";
        String content = "";
        String ticker = "AAPL";
        AddCommentDTO commentaireDTO = new AddCommentDTO(content, ticker);
        facade.addComment(name, commentaireDTO);
    }

    @Test
    public void addInteraction_OK() throws CommentaireInexistantException {
        String name = "user";
        AddInteractionDTO interactionDTO = new AddInteractionDTO(2, Interaction.LIKE);
        Commentaire commentaire = new Commentaire(name, "message", "AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        when(commentaireRepository.save(any())).thenReturn(commentaire);
        CommentaireDTO result = facade.addInteraction(name, interactionDTO);
        assertEquals(commentaire.toDTO(), result);
    }

    @Test(expected = CommentaireInexistantException.class)
    public void addInteraction_KO_CommentaireInexistantException() throws CommentaireInexistantException {
        String name = "user";
        AddInteractionDTO interactionDTO = new AddInteractionDTO(2, Interaction.LIKE);
        when(commentaireRepository.findById(any())).thenReturn(Optional.empty());
        facade.addInteraction(name, interactionDTO);
    }

    @Test
    public void deleteCommentaire_OK() throws AuteurInconnuException, CommentaireInexistantException {
        String username = "user";
        Integer idCommentaire = 1;
        Commentaire commentaire = new Commentaire(username, "message", "AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.deleteComment(username, idCommentaire);
        Mockito.verify(commentaireRepository).delete(commentaire);
    }

    @Test(expected = CommentaireInexistantException.class)
    public void deleteCommentaire_KO_CommentaireInexistantException() throws AuteurInconnuException, CommentaireInexistantException {
        String username = "user";
        Integer idCommentaire = 1;
        when(commentaireRepository.findById(any())).thenReturn(Optional.empty());
        facade.deleteComment(username, idCommentaire);
    }

    @Test(expected = AuteurInconnuException.class)
    public void deleteCommentaire_KO_AuteurNonReconnueException() throws AuteurInconnuException, CommentaireInexistantException {
        String username = "user";
        Integer idCommentaire = 1;
        Commentaire commentaire = new Commentaire("user2", "message", "AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.deleteComment(username, idCommentaire);
    }

    @Test
    public void editCommentaire_OK() throws AuteurInconnuException, PasDeContenuException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("message");
        Commentaire commentaire = new Commentaire(name, "message", "AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        when(commentaireRepository.save(any())).thenReturn(commentaire);
        CommentaireDTO result = facade.editComment(name, idCommentaire, updateCommentaireDTO);
        assertEquals(commentaire.toDTO(), result);
    }

    @Test(expected = CommentaireInexistantException.class)
    public void editCommentaire_KO_CommentaireInexistantException() throws AuteurInconnuException, PasDeContenuException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("message");
        when(commentaireRepository.findById(any())).thenReturn(Optional.empty());
        facade.editComment(name, idCommentaire, updateCommentaireDTO);
    }

    @Test(expected = AuteurInconnuException.class)
    public void editCommentaire_KO_AuteurNonReconnueException() throws AuteurInconnuException, PasDeContenuException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("message");
        Commentaire commentaire = new Commentaire("user2", "message", "AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.editComment(name, idCommentaire, updateCommentaireDTO);

    }

    @Test(expected = PasDeContenuException.class)
    public void editCommentaire_KOContentEmptyException() throws AuteurInconnuException, PasDeContenuException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("");
        Commentaire commentaire = new Commentaire(name, "message", "AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.editComment(name, idCommentaire, updateCommentaireDTO);
    }
}
