package portefeuille.dto.rabbitMq;

public record OrderDto(String action,String username, String ticker , int quantity) {
}
