package com.example.community.dto.request;

import com.example.community.model.Interaction;

public record AddInteractionDTO(Integer idComment, Interaction interaction) {
}
