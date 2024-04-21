package community.dto.request;

import community.model.Interaction;

public record AddInteractionDTO(Integer idComment, Interaction interaction) {
}
