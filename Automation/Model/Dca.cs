﻿using Automation.Model.enums;

namespace Automation.Model;

public class Dca : IAutomation
{
    public string Stock { get; set; }
    public DateTime? LastBuyTime { get; set; }
    public int BuyQuantity { get; set; }
    public Frequency Frequency { get; set; }
    public AutomationType AutomationType { get; set; }

    public Dca(string stock, int buyQuantity, Frequency frequency)
    {
        Stock = stock;
        BuyQuantity = buyQuantity;
        Frequency = frequency;
        LastBuyTime = null;
        AutomationType = AutomationType.Dca;
    }

    public void ExecuteAutomation(string username)
    {
        if (IsAutomationReady())
        {
            // Appeler rabbitMq pour acheter x actions pour le user
            // Mettre à jour le last buy time
        }
    }

    public bool IsAutomationReady()
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