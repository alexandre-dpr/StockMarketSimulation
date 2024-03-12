package com.example.community.controller;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.request.UserDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.AuteurNonReconnueException;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;
import com.example.community.service.FacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/community", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentaireController {
    private final FacadeImpl facade;

    @Autowired
    public CommentaireController(FacadeImpl facade) {
        this.facade = facade;
    }

    @GetMapping(value = "/action/{id}")
    public ResponseEntity<List<CommentaireDTO>> getAllComentaire(Authentication authentication) throws CommentaireInexistantException {
        return ResponseEntity.ok(facade.getAllCommentaire(authentication.getName()));
    }

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<Void> deleteComment(Authentication authentication, @PathVariable Integer id) throws CommentaireInexistantException, AuteurNonReconnueException {

        facade.deleteCommentaire(authentication.getName(),id);
         return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<CommentaireDTO> addComment(Authentication authentication ,@RequestBody AddCommentDTO commentaireDTO){

        return ResponseEntity.ok(facade.addComentaire(new AddCommentDTO(authentication.getName(), commentaireDTO.content(), commentaireDTO.action())));
    }
    @PostMapping(value = "/comment/{id}")
    public ResponseEntity<CommentaireDTO> addInteraction(Authentication authentication,@PathVariable Integer id) throws CommentaireInexistantException {
        return ResponseEntity.ok(facade.addInteraction(authentication.getName(), id ));
    }



}
