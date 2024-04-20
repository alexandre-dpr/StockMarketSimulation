package community.dto.response;

public record CommentaireDTO(Integer id, String userId, String action, String date, String content, Integer nbInteraction, boolean interaction) {
}
