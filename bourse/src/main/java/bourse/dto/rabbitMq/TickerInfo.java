package bourse.dto.rabbitMq;

public record TickerInfo(String uuid,String ticker, Double price) {
}
