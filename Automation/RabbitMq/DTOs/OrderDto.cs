namespace Automation.RabbitMq.DTOs;

public record OrderDto(string action, string username, string ticker, int quantity);