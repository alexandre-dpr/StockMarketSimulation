using System.ComponentModel.DataAnnotations;

namespace Automation.Model;

public class UserAutomation
{
    [Key] public string Username { get; set; }
    public List<Dca> Automations { get; set; }

    public UserAutomation(string username, List<Dca> automations)
    {
        Username = username;
        Automations = automations;
    }

    public UserAutomation(string username)
    {
        Username = username;
        Automations = new List<Dca>();
    }

    public UserAutomation()
    {
    }
}