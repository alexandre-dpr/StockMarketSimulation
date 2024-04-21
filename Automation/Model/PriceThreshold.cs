using Automation.Model.enums;
using Automation.RabbitMq.DTOs;
using Automation.RabbitMq.SenderReceiver;

namespace Automation.Model;

public class PriceThreshold : Automation
{
    public string Ticker { get; init; }
    public int Quantity { get; init; }
    public double ThresholdPrice { get; init; }
    public TransactionType TransactionType { get; init; }
    public ThresholdType ThresholdType { get; init; }

    public PriceThreshold()
    {
    }

    public PriceThreshold(string ticker, double thresholdPrice, TransactionType transactionType,
        ThresholdType thresholdType, int quantity)
    {
        Ticker = ticker;
        ThresholdPrice = thresholdPrice;
        TransactionType = transactionType;
        ThresholdType = thresholdType;
        Quantity = quantity;
        Type = AutomationType.PriceThreshold;
        DeleteAfterExecution = true;
    }

    public override bool Execute(RabbitMqSender rabbitMqSender, string username)
    {
        if (IsReady(rabbitMqSender))
        {
            var order = new OrderDto(TransactionType.ToString(), username, Ticker, Quantity);
            rabbitMqSender.SendOrder(order);
            return true;
        }

        return false;
    }

    public override bool IsReady(RabbitMqSender rabbitMqSender)
    {
        double price = rabbitMqSender.getPrice(new TickerInfoDto(Ticker));
        switch (ThresholdType)
        {
            case ThresholdType.Above:
                return price > ThresholdPrice;

            case ThresholdType.Below:
                return price < ThresholdPrice;

            default:
                return false;
        }
    }
}