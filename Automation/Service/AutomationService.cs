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

    public void AjouterDca(string username, string symbole, int quantite, Frequency frequence)
    {
        var userAutomation = _dbContext.UserAutomations.Find(username);

        if (userAutomation == null)
        {
            userAutomation = new UserAutomation(username);
            _dbContext.UserAutomations.Add(userAutomation);
        }

        userAutomation.Automations.Add(new Dca(symbole, quantite, frequence));
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
}