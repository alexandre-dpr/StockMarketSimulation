using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace Automation.Model;

public class UserAutomation
{
    [Key] [JsonIgnore] public string Username { get; set; }
    public List<Automation> Automations { get; set; }

    public UserAutomation(string username, List<Automation> automations)
    {
        Username = username;
        Automations = automations;
    }

    public UserAutomation(string username)
    {
        Username = username;
        Automations = new List<Automation>();
    }

    public UserAutomation()
    {
    }
}