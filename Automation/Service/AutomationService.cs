using Automation.Model;
using Automation.Model.enums;
using Automation.RabbitMq.SenderReceiver;
using Automation.Repository;
using Microsoft.EntityFrameworkCore;

namespace Automation.Service;

public class AutomationService
{
    private readonly UserAutomationDbContext _dbContext;

    private readonly RabbitMqSender _rabbitMqSender;

    public AutomationService(UserAutomationDbContext automationDbContext)
    {
        _dbContext = automationDbContext;
        _rabbitMqSender = new RabbitMqSender();
    }

    public void AjouterDca(string username, string ticker, int quantite, Frequency frequence,
        TransactionType transactionType)
    {
        AddToAutomations(username, new Dca(ticker, quantite, frequence, transactionType));
    }

    public void AjouterPriceThreshold(string ticker, double thresholdPrice, TransactionType action,
        ThresholdType thresholdType, int quantity, string username)
    {
        AddToAutomations(username, new PriceThreshold(ticker, thresholdPrice, action, thresholdType, quantity));
    }


    public UserAutomation GetAutomations(string username)
    {
        var userAutomation = _dbContext.UserAutomations
            .Include(ua => ua.Automations)
            .FirstOrDefault(ua => ua.Username == username);

        return userAutomation ?? new UserAutomation(username);
    }

    public void DeleteAutomation(int id, string username)
    {
        _dbContext.UserAutomations
            .Include(ua => ua.Automations)
            .FirstOrDefault(ua => ua.Username == username)
            ?.Automations.RemoveAll(a => a.Id == id);
    }

    private void AddToAutomations(string username, Model.Automation automation)
    {
        var userAutomation = _dbContext.UserAutomations.Find(username);

        if (userAutomation == null)
        {
            userAutomation = new UserAutomation(username);
            _dbContext.UserAutomations.Add(userAutomation);
        }

        userAutomation.Automations.Add(automation);
        _dbContext.SaveChanges();
    }

    public void ExecuteAutomations()
    {
        _dbContext.UserAutomations
            .Include(ua => ua.Automations)
            .ToList()
            .ForEach(userAutomation => userAutomation.Automations
                .ForEach(automation =>
                {
                    automation.Execute(_rabbitMqSender, userAutomation.Username);
                    if (automation.DeleteAfterExecution)
                    {
                        DeleteAutomation(automation.Id, userAutomation.Username);
                    }
                }));
        _dbContext.SaveChanges();
    }
}