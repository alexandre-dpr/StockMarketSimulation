package community.controller;

import community.dto.request.AddCommentDTO;
import community.dto.request.AddInteractionDTO;
import community.dto.request.UpdateCommentaireDTO;
import community.dto.response.CommentaireDTO;
import community.exceptions.AuteurInconnuException;
import community.exceptions.CommentaireInexistantException;
import community.exceptions.PasDeContenuException;
import community.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/community", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentaireController {

    @Autowired
    private CommunityService communityService;

    @GetMapping(value = "/{ticker}")
    public ResponseEntity<List<CommentaireDTO>> getComments(@PathVariable String ticker) {
        return ResponseEntity.ok(communityService.getComments(ticker));
    }

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<Void> deleteComment(Authentication authentication, @PathVariable Integer id) throws CommentaireInexistantException, AuteurInconnuException {
        communityService.deleteComment(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/comment/{id}")
    public ResponseEntity<CommentaireDTO> editComment(Authentication authentication, @PathVariable Integer id, @RequestBody UpdateCommentaireDTO updateCommentaireDTO) throws PasDeContenuException, CommentaireInexistantException, AuteurInconnuException {
        return ResponseEntity.ok(communityService.editComment(authentication.getName(), id, updateCommentaireDTO));
    }

    @PostMapping(value = "/comment")
    public ResponseEntity<CommentaireDTO> addComment(Authentication authentication, @RequestBody AddCommentDTO commentaireDTO) throws PasDeContenuException {
        return ResponseEntity.ok(communityService.addComment(authentication.getName(), commentaireDTO));
    }

    @PostMapping(value = "/interaction")
    public ResponseEntity<CommentaireDTO> addInteraction(Authentication authentication, @RequestBody AddInteractionDTO interactionDTO) throws CommentaireInexistantException {
        return ResponseEntity.ok(communityService.addInteraction(authentication.getName(), interactionDTO));
    }
}
