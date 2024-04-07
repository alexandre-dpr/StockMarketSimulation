package com.example.community.controller;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.request.AddInteractionDTO;
import com.example.community.dto.request.UpdateCommentaireDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.AuteurNonReconnueException;
import com.example.community.exceptions.CommentaireInexistantException;
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
    @Autowired
    private FacadeImpl facade;

    @GetMapping(value = "/{ticker}")
    public ResponseEntity<List<CommentaireDTO>> getAllComentaire(@PathVariable String ticker, Authentication authentication)  {
        return ResponseEntity.ok(facade.getAllCommentaire(authentication.getName(),ticker));
    }

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<Void> deleteComment(Authentication authentication, @PathVariable Integer id) throws CommentaireInexistantException, AuteurNonReconnueException {
        facade.deleteCommentaire(authentication.getName(),id);
         return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/comment/{id}")
    public ResponseEntity<CommentaireDTO> editComment(Authentication authentication,@PathVariable Integer id ,@RequestBody UpdateCommentaireDTO updateCommentaireDTO) throws CommentaireInexistantException, AuteurNonReconnueException {
        return ResponseEntity.ok(facade.editCommentaire(authentication.getName(), id,updateCommentaireDTO ));
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<CommentaireDTO> addComment(Authentication authentication , @RequestBody AddCommentDTO commentaireDTO){
        return ResponseEntity.ok(facade.addComentaire(authentication.getName(),commentaireDTO));
    }

    @PostMapping(value = "/interaction")
    public ResponseEntity<CommentaireDTO> addInteraction(Authentication authentication, @RequestBody AddInteractionDTO interactionDTO) throws CommentaireInexistantException {
        return ResponseEntity.ok(facade.addInteraction(authentication.getName(), interactionDTO ));
    }
}
