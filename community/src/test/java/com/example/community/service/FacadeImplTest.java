package com.example.community.service;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.request.AddInteractionDTO;
import com.example.community.dto.request.UpdateCommentaireDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.AuteurNonReconnueException;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.exceptions.ContentEmptyException;
import com.example.community.model.Commentaire;
import com.example.community.model.Interaction;
import com.example.community.repository.CommentaireRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class FacadeImplTest {

    @Mock
    private CommentaireRepository commentaireRepository;

    @InjectMocks
    private FacadeImpl facade;


    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getAllCommentaire_OK() {
        String action = "AAPL";
        List<Commentaire> commentaireList = new ArrayList<>();
        List<CommentaireDTO> commentaireDTOList = new ArrayList<>();
        commentaireList.add(new Commentaire("user","blabla",action));
        commentaireList.add(new Commentaire("user","blabla2",action));
        commentaireList.forEach(e->{
            commentaireDTOList.add(e.toDTO());
        });
        when(commentaireRepository.findAllByActionOrderByDateDesc(action)).thenReturn(Optional.of(commentaireList));
        List<CommentaireDTO> result = facade.getAllCommentaire("damien", action);
        assertEquals(commentaireDTOList, result);
    }

    @Test
    public void getAllCommentaire_Empty_OK() {
        String action = "someAction";
        when(commentaireRepository.findAllByActionOrderByDateDesc(action)).thenReturn(Optional.empty());
        List<CommentaireDTO> result = facade.getAllCommentaire("someName", action);
        assertEquals(0, result.size());
    }

    @Test
    public void addComentaire_OK() throws ContentEmptyException {
        String name = "user";
        String content = "Ceci est un commentaire";
        String ticker = "AAPL";
        AddCommentDTO commentaireDTO = new AddCommentDTO(content,ticker);
        Commentaire commentaire = new Commentaire(name, commentaireDTO.content(), commentaireDTO.ticker());
        when(commentaireRepository.save(commentaire)).thenReturn(commentaire);
        CommentaireDTO result = facade.addComentaire(name, commentaireDTO);
        assertNotNull(result);
        assertEquals(commentaire.toDTO(), result);
    }

    @Test(expected = ContentEmptyException.class)
    public void addComentaire_KO_ContentEmptyException() throws ContentEmptyException {
        String name = "user";
        String content = "";
        String ticker = "AAPL";
        AddCommentDTO commentaireDTO = new AddCommentDTO(content,ticker);
        facade.addComentaire(name, commentaireDTO);
    }

    @Test
    public void addInteraction_OK() throws CommentaireInexistantException {
        String name = "user";
        AddInteractionDTO interactionDTO = new AddInteractionDTO(2, Interaction.LIKE);
        Commentaire commentaire = new Commentaire(name,"message","AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        when(commentaireRepository.save(any())).thenReturn(commentaire);
        CommentaireDTO result = facade.addInteraction(name,interactionDTO);
        assertEquals(commentaire.toDTO(), result);
    }

    @Test(expected = CommentaireInexistantException.class)
    public void addInteraction_KO_CommentaireInexistantException() throws CommentaireInexistantException {
        String name = "user";
        AddInteractionDTO interactionDTO = new AddInteractionDTO(2, Interaction.LIKE);
        when(commentaireRepository.findById(any())).thenReturn(Optional.empty());
        facade.addInteraction(name,interactionDTO);
    }

    @Test
    public void deleteCommentaire_OK() throws AuteurNonReconnueException, CommentaireInexistantException {
        String username = "user";
        Integer idCommentaire = 1;
        Commentaire commentaire = new Commentaire(username,"message","AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.deleteCommentaire(username,idCommentaire);
        Mockito.verify(commentaireRepository).delete(commentaire);
    }

    @Test(expected = CommentaireInexistantException.class)
    public void deleteCommentaire_KO_CommentaireInexistantException() throws AuteurNonReconnueException, CommentaireInexistantException {
        String username = "user";
        Integer idCommentaire = 1;
        when(commentaireRepository.findById(any())).thenReturn(Optional.empty());
        facade.deleteCommentaire(username,idCommentaire);
    }

    @Test(expected = AuteurNonReconnueException.class)
    public void deleteCommentaire_KO_AuteurNonReconnueException() throws AuteurNonReconnueException, CommentaireInexistantException {
        String username = "user";
        Integer idCommentaire = 1;
        Commentaire commentaire = new Commentaire("user2","message","AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.deleteCommentaire(username,idCommentaire);
    }

    @Test
    public void editCommentaire_OK() throws AuteurNonReconnueException, ContentEmptyException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("message");
        Commentaire commentaire = new Commentaire(name,"message","AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        when(commentaireRepository.save(any())).thenReturn(commentaire);
        CommentaireDTO result = facade.editCommentaire(name,idCommentaire,updateCommentaireDTO);
        assertEquals(commentaire.toDTO(), result);
    }

    @Test(expected = CommentaireInexistantException.class)
    public void editCommentaire_KO_CommentaireInexistantException() throws AuteurNonReconnueException, ContentEmptyException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("message");
        Commentaire commentaire = new Commentaire(name,"message","AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.empty());
        facade.editCommentaire(name,idCommentaire,updateCommentaireDTO);
    }

    @Test(expected = AuteurNonReconnueException.class)
    public void editCommentaire_KO_AuteurNonReconnueException() throws AuteurNonReconnueException, ContentEmptyException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("message");
        Commentaire commentaire = new Commentaire("user2","message","AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.editCommentaire(name,idCommentaire,updateCommentaireDTO);

    }

    @Test(expected =ContentEmptyException.class)
    public void editCommentaire_KOContentEmptyException() throws AuteurNonReconnueException, ContentEmptyException, CommentaireInexistantException {
        String name = "user";
        Integer idCommentaire = 1;
        UpdateCommentaireDTO updateCommentaireDTO = new UpdateCommentaireDTO("");
        Commentaire commentaire = new Commentaire(name,"message","AAPL");
        when(commentaireRepository.findById(any())).thenReturn(Optional.of(commentaire));
        facade.editCommentaire(name,idCommentaire,updateCommentaireDTO);
    }



}
