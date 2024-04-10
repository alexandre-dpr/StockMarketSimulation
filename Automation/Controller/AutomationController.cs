using Automation.Model.enums;
using Automation.Service;
using Microsoft.AspNetCore.Mvc;

namespace Automation.Controller;

[ApiController]
[Route("/api/[controller]")]
[Produces("application/json")]
public class AutomationController : ControllerBase
{
    private readonly AutomationService _automationService;

    public AutomationController(AutomationService automationService)
    {
        _automationService = automationService;
    }

    [HttpGet]
    public IActionResult GetAutomations(string username)
    {
        return Ok(_automationService.GetAutomations(username));
    }

    [HttpPost]
    public IActionResult PostAutomations(string username, string symbole, int quantite, Frequency frequence)
    {
        _automationService.AjouterAutomationAsync(username, symbole, quantite, frequence);
        return Ok();
    }
}