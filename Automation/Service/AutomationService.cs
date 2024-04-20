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
        var automation = AddToAutomations(username, new Dca(ticker, quantite, frequence, transactionType));
        ExecuteAutomation(automation, username);
        _dbContext.SaveChanges();
    }

    public void AjouterPriceThreshold(string ticker, double thresholdPrice, TransactionType action,
        ThresholdType thresholdType, int quantity, string username)
    {
        var automation = AddToAutomations(username,
            new PriceThreshold(ticker, thresholdPrice, action, thresholdType, quantity));
        ExecuteAutomation(automation, username);
        _dbContext.SaveChanges();
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

    private Model.Automation AddToAutomations(string username, Model.Automation automation)
    {
        var userAutomation = _dbContext.UserAutomations.Find(username);

        if (userAutomation == null)
        {
            userAutomation = new UserAutomation(username);
            _dbContext.UserAutomations.Add(userAutomation);
        }

        userAutomation.Automations.Add(automation);
        _dbContext.SaveChanges();

        return automation;
    }

    private void ExecuteAutomations(object? state)
    {
        _dbContext.UserAutomations
            .Include(ua => ua.Automations)
            .ToList()
            .ForEach(userAutomation => userAutomation.Automations
                .ForEach(automation => { ExecuteAutomation(automation, userAutomation.Username); }));
        _dbContext.SaveChanges();
    }

    private void ExecuteAutomation(Model.Automation automation, string username)
    {
        bool isExecuted = automation.Execute(_rabbitMqSender, username);
        if (isExecuted && automation.DeleteAfterExecution)
        {
            DeleteAutomation(automation.Id, username);
        }
    }
}