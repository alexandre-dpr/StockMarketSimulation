using Automation.Model.enums;
using Automation.RabbitMq.DTOs;
using Automation.RabbitMq.SenderReceiver;

namespace Automation.Model;

public class Dca : Automation
{
    public string Ticker { get; init; }
    public DateTime? LastBuyTime { get; set; }
    public int BuyQuantity { get; init; }
    public Frequency Frequency { get; init; }
    public TransactionType TransactionType { get; init; }

    public Dca(string ticker, int buyQuantity, Frequency frequency, TransactionType transactionType)
    {
        Ticker = ticker;
        BuyQuantity = buyQuantity;
        Frequency = frequency;
        TransactionType = transactionType;
        LastBuyTime = null;
        Type = AutomationType.Dca;
        DeleteAfterExecution = false;
    }

    public Dca()
    {
    }

    public override void Execute(RabbitMQSender rabbitMqSender, string username)
    {
        if (IsReady(rabbitMqSender))
        {
            var achat = new OrderDto(TransactionType.ToString(), username, Ticker, BuyQuantity);
            rabbitMqSender.SendOrder(achat);
            LastBuyTime = DateTime.Now;
        }
    }

    public override bool IsReady(RabbitMQSender _)
    {
        if (LastBuyTime == null) return true;
        var now = DateTime.Now;
        switch (Frequency)
        {
            case Frequency.Weekly:
                return (LastBuyTime.Value - now).TotalDays > 7;

            case Frequency.Monthly:
                return (LastBuyTime.Value - now).TotalDays > 30;

            default:
                return false;
        }
    }
}