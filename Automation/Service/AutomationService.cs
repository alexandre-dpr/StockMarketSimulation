using Automation.Model;
using Automation.Model.enums;
using Automation.Repository;
using Microsoft.EntityFrameworkCore;

namespace Automation.Service;

public class AutomationService
{
    private readonly UserAutomationDbContext _dbContext;

    public AutomationService(UserAutomationDbContext automationDbContext)
    {
        _dbContext = automationDbContext;
    }

    public void AjouterDca(string username, string ticker, int quantite, Frequency frequence)
    {
        AddToAutomations(username, new Dca(ticker, quantite, frequence));
    }

    public void AjouterPriceThreshold(string ticker, double thresholdPrice, TransactionType action,
        ThresholdType thresholdType, string username)
    {
        AddToAutomations(username, new PriceThreshold(ticker, thresholdPrice, action, thresholdType));
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
}