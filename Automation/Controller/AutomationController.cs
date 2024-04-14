using Automation.Dto.Request;
using Automation.Service;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Automation.Controller;

[ApiController]
[Route("/api/[controller]")]
[Produces("application/json")]
[Authorize]
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
        var username = User.Claims.ElementAtOrDefault(1)?.Value;
        if (username == null)
        {
            return Unauthorized();
        }

        return Ok(_automationService.GetAutomations(username));
    }

    [HttpDelete]
    public IActionResult SupprimerAutomation(int idAutomation)
    {
        var username = User.Claims.ElementAtOrDefault(1)?.Value;
        if (username == null)
        {
            return Unauthorized();
        }

        _automationService.DeleteAutomation(idAutomation, username);
        return NoContent();
    }

    [HttpPost("/api/automation/dca")]
    public IActionResult AjouterDca([FromBody] DcaReqDto req)
    {
        var username = User.Claims.ElementAtOrDefault(1)?.Value;
        if (username == null)
        {
            return Unauthorized();
        }

        _automationService.AjouterDca(username, req.Symbole, req.Quantite, req.Frequence);
        return Ok();
    }

    [HttpPost("/api/automation/pricethreshold")]
    public IActionResult AjouterPriceThreshold([FromBody] PriceThresholdReqDto req)
    {
        var username = User.Claims.ElementAtOrDefault(1)?.Value;
        if (username == null)
        {
            return Unauthorized();
        }

        _automationService.AjouterPriceThreshold(req.Ticker, req.ThresholdPrice, req.TransactionType, req.ThresholdType,
            username);
        return Ok();
    }
}