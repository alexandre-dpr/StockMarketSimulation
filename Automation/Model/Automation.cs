using System.ComponentModel.DataAnnotations;
using Automation.Model.enums;
using Automation.RabbitMq.SenderReceiver;
using Newtonsoft.Json;

namespace Automation.Model;

public abstract class Automation
{
    [Key] public int Id { get; set; }

    [JsonIgnore] public UserAutomation Parent { get; }

    public AutomationType Type { get; set; }

    [JsonIgnore] public bool DeleteAfterExecution { get; set; }

    /**
     * Execute l'automation
     * @return true si l'automation a été exécutée, false sinon
     */
    public abstract bool Execute(RabbitMqSender rabbitMqSender, string username);

    /**
     * Vérifie si l'automation est prête à être exécutée
     * @return true si l'automation est prête, false sinon
     */
    public abstract bool IsReady(RabbitMqSender rabbitMqSender);
}