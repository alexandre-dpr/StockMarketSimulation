namespace Automation.Service
{
    public class ScheduledTaskService : IHostedService
    {
        private Timer _timer;
        private readonly AutomationService _automationService;
        private const int AutomationExecutionInterval = 5;

        public ScheduledTaskService(AutomationService automationService)
        {
            _automationService = automationService;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            _timer = new Timer(_automationService.ExecuteAutomations, null, TimeSpan.FromMinutes(1),
                TimeSpan.FromMinutes(AutomationExecutionInterval));
            return Task.CompletedTask;
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            _timer?.Dispose();
            return Task.CompletedTask;
        }
    }
}