package com.example.community.controller;

import com.example.community.dto.request.AddCommentDTO;
import com.example.community.dto.request.UserDTO;
import com.example.community.dto.response.CommentaireDTO;
import com.example.community.exceptions.CommentaireInexistantException;
import com.example.community.model.Commentaire;
import com.example.community.service.FacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CommentaireDTO>> getAllComentaire(@PathVariable String id) throws CommentaireInexistantException {
        return ResponseEntity.ok(facade.getAllCommentaire(id));
    }

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) throws CommentaireInexistantException {
         facade.deleteCommentaire(id);
         return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<CommentaireDTO> addComment(@RequestBody AddCommentDTO commentaireDTO){
        return ResponseEntity.ok(facade.addComentaire(commentaireDTO));
    }
    @PostMapping(value = "/comment/{id}")
    public ResponseEntity<CommentaireDTO> addInteraction(@PathVariable Integer id, @RequestBody UserDTO userDTO) throws CommentaireInexistantException {
        return ResponseEntity.ok(facade.addInteraction(userDTO.userId(), id ));
    }



}
