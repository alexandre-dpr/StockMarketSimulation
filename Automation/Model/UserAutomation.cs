using System.ComponentModel.DataAnnotations;

namespace Automation.Model;

public class UserAutomation
{
    [Key] public string Username { get; set; }
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