using Automation.Dto.Request;
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

    [HttpDelete]
    public IActionResult SupprimerAutomation(int idAutomation, string username)
    {
        _automationService.DeleteAutomation(idAutomation, username);
        return NoContent();
    }

    [HttpPost]
    public IActionResult AjouterDca([FromBody] DcaReqDto req)
    {
        _automationService.AjouterDca(req.Username, req.Symbole, req.Quantite, req.Frequence);
        return Ok();
    }
}