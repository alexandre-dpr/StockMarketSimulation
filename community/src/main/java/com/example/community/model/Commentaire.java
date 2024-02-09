package com.example.community.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class Commentaire {

    private Integer id;
    private Integer user;
    private LocalDate date;
    private String content;
    private Map<Integer, Interaction> interactions;

    public Commentaire(Integer user, String content){
        this.id = 1;
        this.user = user;
        this.date = LocalDate.now();
        this.content = content;
        this.interactions = new HashMap<>();
    }



}
