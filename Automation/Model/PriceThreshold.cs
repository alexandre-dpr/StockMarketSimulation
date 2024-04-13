using Automation.Model.enums;

namespace Automation.Model;

public class PriceThreshold : Automation
{
    public string Ticker { get; set; }
    public double ThresholdPrice { get; set; }
    public TransactionType TransactionType { get; set; }
    public ThresholdType ThresholdType { get; set; }

    public PriceThreshold()
    {
    }

    public PriceThreshold(string ticker, double thresholdPrice, TransactionType transactionType,
        ThresholdType thresholdType)
    {
        Ticker = ticker;
        ThresholdPrice = thresholdPrice;
        TransactionType = transactionType;
        ThresholdType = thresholdType;
        AutomationType = AutomationType.PriceThreshold;
    }

    public override void ExecuteAutomation(string username)
    {
        // TODO
        throw new NotImplementedException();
    }

    public override bool IsAutomationReady()
    {
        // TODO
        throw new NotImplementedException();
    }
}