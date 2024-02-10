package com.example.community.model;


import com.example.community.dto.response.CommentaireDTO;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Commentaire {

    @Id
    @GeneratedValue
    private Integer id;
    private String user;

    private String action;
    private LocalDate date;
    private String content;
    @ElementCollection
    private Map<String, Interaction> interactions;

    public Commentaire(String user, String content, String action){
        this.user = user;
        this.action = action;
        this.date = LocalDate.now();
        this.content = content;
        this.interactions = new HashMap<>();
    }

    public void addInteraction(String userId){
        if(interactions.containsKey(userId)){
            interactions.remove(userId);
        }else {
            interactions.put(userId, Interaction.LIKE);
        }
    }

    public CommentaireDTO toDTO(){
        return new CommentaireDTO(this.id,this.user,this.action,this.date.toString(),this.content,interactions.size());
    }



}
