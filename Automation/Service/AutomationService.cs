using Automation.Model;
using Automation.Model.enums;
using Automation.Repository;

namespace Automation.Service;

public class AutomationService
{
    private readonly UserAutomationDbContext _dbContext;

    public AutomationService(UserAutomationDbContext automationDbContext)
    {
        _dbContext = automationDbContext;
    }

    public async Task AjouterAutomationAsync(string username, string symbole, int quantite, Frequency frequence)
    {
        // Rechercher l'utilisateur
        var userAutomation = await _dbContext.UserAutomations.FindAsync(username);

        // Si l'utilisateur n'existe pas, le créer
        if (userAutomation == null)
        {
            userAutomation = new UserAutomation(username);
            _dbContext.UserAutomations.Add(userAutomation);
        }

        // Créer une nouvelle DCA
        var dca = new Dca(symbole, quantite, frequence);

        // Ajouter la DCA à la collection d'automations de l'utilisateur
        userAutomation.Automations.Add(dca);

        // Enregistrer les modifications
        await _dbContext.SaveChangesAsync();
    }


    public UserAutomation GetAutomations(string username)
    {
        var userAutomation = _dbContext.UserAutomations.Find(username);
        if (userAutomation != null)
        {
            _dbContext.Entry(userAutomation).Collection(ua => ua.Automations).Load();
        }

        return userAutomation;
    }
}