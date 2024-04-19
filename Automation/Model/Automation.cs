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

    public bool DeleteAfterExecution { get; set; }

    public abstract void Execute(RabbitMqSender rabbitMqSender, string username);

    public abstract bool IsReady(RabbitMqSender rabbitMqSender);
}