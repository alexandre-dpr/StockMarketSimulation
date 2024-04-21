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

    /**
     * Ajoute une automation de type DCA
     * @param username Le nom de l'utilisateur
     * @param ticker Le ticker de l'action
     * @param quantite La quantité de l'action
     * @param frequence La fréquence de l'achat
     * @param transactionType Le type de transaction
     */
    public void AjouterDca(string username, string ticker, int quantite, Frequency frequence,
        TransactionType transactionType)
    {
        var automation = AddToAutomations(username, new Dca(ticker, quantite, frequence, transactionType));
        ExecuteAutomation(automation, username);
        _dbContext.SaveChanges();
    }

    /**
     * Ajoute une automation de type PriceThreshold
     * @param username Le nom de l'utilisateur
     * @param ticker Le ticker de l'action
     * @param thresholdPrice Le prix seuil
     * @param action L'action à effectuer
     * @param thresholdType Le type de seuil
     * @param quantity La quantité de l'action
     */
    public void AjouterPriceThreshold(string ticker, double thresholdPrice, TransactionType action,
        ThresholdType thresholdType, int quantity, string username)
    {
        var automation = AddToAutomations(username,
            new PriceThreshold(ticker, thresholdPrice, action, thresholdType, quantity));
        ExecuteAutomation(automation, username);
        _dbContext.SaveChanges();
    }

    /**
     * Récupère les automations d'un utilisateur
     * @param username Le nom de l'utilisateur
     * @return Les automations de l'utilisateur
     */
    public UserAutomation GetAutomations(string username)
    {
        var userAutomation = _dbContext.UserAutomations
            .Include(ua => ua.Automations)
            .FirstOrDefault(ua => ua.Username == username);

        return userAutomation ?? new UserAutomation(username);
    }

    /**
     * Supprime une automation
     * @param id L'id de l'automation
     * @param username Le nom de l'utilisateur
     */
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

    /**
     * Exécute les automations
     * @param state L'état de l'automate
     */
    public void ExecuteAutomations(object? state)
    {
        var userAutomations = _dbContext.UserAutomations
            .Include(ua => ua.Automations)
            .ToList();

        for (int i = 0; i < userAutomations.Count; i++)
        {
            var userAutomation = userAutomations[i];
            for (int j = 0; j < userAutomation.Automations.Count; j++)
            {
                var automation = userAutomation.Automations[j];
                ExecuteAutomation(automation, userAutomation.Username);
            }
        }

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