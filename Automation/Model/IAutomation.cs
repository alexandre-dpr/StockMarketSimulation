using System.ComponentModel.DataAnnotations;
using Automation.Model.enums;
using Newtonsoft.Json;

namespace Automation.Model;

public interface IAutomation
{
    [Key] public int Id { get; }

    [JsonIgnore] public UserAutomation Parent { get; }

    public AutomationType AutomationType { get; }

    void ExecuteAutomation(string username);

    Boolean IsAutomationReady();
}