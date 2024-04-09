using Automation.Model.enums;

namespace Automation.Model;

public interface IAutomation
{
    public AutomationType AutomationType { get; set; }

    void ExecuteAutomation(string username);

    Boolean IsAutomationReady();
}