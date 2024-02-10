package com.example.community.service;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;
import com.example.community.repository.CommentaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacadeImplTest {

    @Mock
    private CommentaireRepository commentaireRepository;

    @InjectMocks
    private FacadeImpl facade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCommentaire() {
        List<Commentaire> commentaires = new ArrayList<>();
        commentaires.add(new Commentaire("user1", "content1", "action1"));
        commentaires.add(new Commentaire("user2", "content2", "action1"));
        when(commentaireRepository.findAllByAction("action1")).thenReturn(Optional.of(commentaires));

        try {
            List<CommentaireDTO> commentaireDTOS = facade.getAllCommentaire("action1");
            assertEquals(2, commentaireDTOS.size());
        } catch (CommentaireInexistantException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddComentaire() {
        AddCommentDTO addCommentDTO = new AddCommentDTO("user1", "content1", "action1");
        Commentaire commentaire = new Commentaire("user1", "content1", "action1");
        when(commentaireRepository.save(commentaire)).thenReturn(commentaire);

        CommentaireDTO commentaireDTO = facade.addComentaire(addCommentDTO);

        assertEquals("user1", commentaireDTO.userId());
        assertEquals("content1", commentaireDTO.content());
        assertEquals("action1", commentaireDTO.action());
    }

    @Test
    public void testAddInteraction() {
        Commentaire commentaire = new Commentaire("user1", "content1", "action1");
        when(commentaireRepository.findById(1)).thenReturn(Optional.of(commentaire));

        try {
            CommentaireDTO commentaireDTO = facade.addInteraction("user1", 1);
            assertEquals(1, commentaireDTO.interaction());
        } catch (CommentaireInexistantException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteCommentaire() {
        Commentaire commentaire = new Commentaire("user1", "content1", "action1");
        when(commentaireRepository.findById(1)).thenReturn(Optional.of(commentaire));

        assertDoesNotThrow(() -> facade.deleteCommentaire(1));
    }

    @Test
    public void testDeleteCommentaire_NotFound() {
        when(commentaireRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CommentaireInexistantException.class, () -> facade.deleteCommentaire(1));
    }
}
