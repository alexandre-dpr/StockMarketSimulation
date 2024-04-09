using Automation.Model;
using Automation.Model.enums;

namespace Automation.Service;

public class AutomationService
{
    public UserAutomation GetAutomations()
    {
        return new UserAutomation("Alex", new List<IAutomation> { new Dca("AAPL", 2, Frequency.Weekly) });
    }
}