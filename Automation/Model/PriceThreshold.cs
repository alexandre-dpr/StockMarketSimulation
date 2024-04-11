using Automation.Model.enums;

namespace Automation.Model;

public class PriceThreshold : Automation
{
    public double ThresholdPrice { get; set; }
    public TransactionType TransactionType { get; set; }
    public ThresholdType ThresholdType { get; set; }

    public PriceThreshold()
    {
    }

    public PriceThreshold(double thresholdPrice, TransactionType transactionType, ThresholdType thresholdType)
    {
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