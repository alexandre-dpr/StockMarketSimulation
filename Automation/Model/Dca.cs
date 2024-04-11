﻿using Automation.Model.enums;

namespace Automation.Model;

public class Dca : Automation
{
    public string Stock { get; set; }
    public DateTime? LastBuyTime { get; set; }
    public int BuyQuantity { get; set; }
    public Frequency Frequency { get; set; }

    public Dca(string stock, int buyQuantity, Frequency frequency)
    {
        Stock = stock;
        BuyQuantity = buyQuantity;
        Frequency = frequency;
        LastBuyTime = null;
        AutomationType = AutomationType.Dca;
    }

    public Dca()
    {
    }

    public override void ExecuteAutomation(string username)
    {
        if (IsAutomationReady())
        {
            // Appeler rabbitMq pour acheter x actions pour le user
            // Mettre à jour le last buy time
        }
    }

    public override bool IsAutomationReady()
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