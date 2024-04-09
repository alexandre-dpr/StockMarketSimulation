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
    public IActionResult GetAutomations()
    {
        return Ok(_automationService.GetAutomations());
    }
}