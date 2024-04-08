package com.example.community.dto.response;

import com.example.community.model.Interaction;

public record CommentaireDTO(Integer id, String userId, String action, String date, String content, Integer nbInteraction, boolean interaction) {
}
