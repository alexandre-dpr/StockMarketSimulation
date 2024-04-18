namespace Automation.RabbitMq.DTOs;

public record OrderDto(string Action, string Username, string Ticker, int Quantity);