namespace Automation.Model;

public class UserAutomation
{
    public string User { get; set; }
    public List<IAutomation> Automations { get; set; }

    public UserAutomation(string user, List<IAutomation> automations)
    {
        User = user;
        Automations = automations;
    }
}