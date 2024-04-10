using Automation.Model.enums;
using Automation.Service;
using Microsoft.AspNetCore.Authentication;
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
    public IActionResult GetAutomations(string username,HttpContext httpContext)
    {
        var authenticateResult =  httpContext.AuthenticateAsync().Result;
        if (!authenticateResult.Succeeded)
        {
            httpContext.Response.StatusCode = StatusCodes.Status401Unauthorized;
            return Unauthorized();
        }   
        
        return Ok(_automationService.GetAutomations(authenticateResult.Principal.Identity.Name));
    }

    [HttpPost]
    public IActionResult PostAutomations(string username, string symbole, int quantite, Frequency frequence,HttpContext httpContext)
    {
        var authenticateResult =  httpContext.AuthenticateAsync().Result;
        if (!authenticateResult.Succeeded)
        {
            httpContext.Response.StatusCode = StatusCodes.Status401Unauthorized;
            return Unauthorized();
        }   
        _automationService.AjouterAutomationAsync(authenticateResult.Principal.Identity.Name, symbole, quantite, frequence);
        return Ok();
    }
}