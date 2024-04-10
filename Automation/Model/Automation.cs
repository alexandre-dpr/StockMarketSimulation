using System.ComponentModel.DataAnnotations;
using Automation.Model.enums;
using Newtonsoft.Json;

namespace Automation.Model;

public abstract class Automation
{
    [Key] public int Id { get; set; }

    [JsonIgnore] public UserAutomation Parent { get; }

    public AutomationType AutomationType { get; }

    public abstract void ExecuteAutomation(string username);

    public abstract Boolean IsAutomationReady();
}