package com.example.community.controller;

import com.example.community.dto.request.CommentaireDTO;
import com.example.community.dto.request.UserDTO;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;
import com.example.community.service.FacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController()
@RequestMapping(value = "/community/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentaireController {
    private final FacadeImpl facade;

    @Autowired
    public CommentaireController(FacadeImpl facade) {
        this.facade = facade;
    }

    @GetMapping(value = "/comment")
    public ResponseEntity<ArrayList<Commentaire>> getAllComentaire(){
        return ResponseEntity.ok(facade.getAllCommentaire());
    }

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) throws CommentaireInexistantException {
         facade.deleteCommentaire(id);
         return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<Commentaire> addComment(@RequestBody CommentaireDTO commentaireDTO){
        return ResponseEntity.ok(facade.addComentaire(commentaireDTO.userId(),commentaireDTO.content()));
    }
    @PostMapping(value = "/comment/{id}")
    public ResponseEntity<Commentaire> addInteraction(@PathVariable Integer id, @RequestBody UserDTO userDTO) throws CommentaireInexistantException {
        return ResponseEntity.ok(facade.addInteraction(userDTO.userId(), id ));
    }



}
