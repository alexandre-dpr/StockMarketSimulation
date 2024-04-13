using Automation.Model;
using Automation.Model.enums;
using Automation.Repository;
using Automation.Service;
using Microsoft.EntityFrameworkCore;
using Moq;
using NUnit.Framework;

namespace Automation.Tests;

[TestFixture]
public class AutomationServiceTests
{
    private Mock<UserAutomationDbContext> _mockDbContext;
    private AutomationService _service;

    [SetUp]
    public void SetUp()
    {
        _mockDbContext = new Mock<UserAutomationDbContext>();
        _service = new AutomationService(_mockDbContext.Object);
    }

    [Test]
    public void AjouterDca()
    {
        const string username = "test";
        const string ticker = "AAPL";
        const int quantite = 10;
        const Frequency frequence = Frequency.Weekly;

        _mockDbContext.Setup(x => x.UserAutomations.Find(username)).Returns((UserAutomation?)null);

        _service.AjouterDca(username, ticker, quantite, frequence);
        _mockDbContext.Verify(x => x.SaveChanges(), Times.Once);
    }

    [Test]
    public void AjouterPriceThreshold()
    {
        const int thresholdPrice = 100;
        const string ticker = "AAPL";
        const TransactionType action = TransactionType.Buy;
        const ThresholdType thresholdType = ThresholdType.Above;
        const string username = "test";

        _mockDbContext.Setup(x => x.UserAutomations.Find(username)).Returns((UserAutomation?)null);

        _service.AjouterPriceThreshold(ticker, thresholdPrice, action, thresholdType, username);
        _mockDbContext.Verify(x => x.SaveChanges(), Times.Once);
    }

    [Test]
    public void GetAutomations()
    {
        const string username = "test";

        var dca = new Dca("AAPL", 10, Frequency.Weekly);
        var userAutomation = new UserAutomation(username, [dca]);

        var userAutomations = new List<UserAutomation> { userAutomation }.AsQueryable();

        var mockSet = new Mock<DbSet<UserAutomation>>();
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.Provider).Returns(userAutomations.Provider);
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.Expression).Returns(userAutomations.Expression);
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.ElementType).Returns(userAutomations.ElementType);
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.GetEnumerator()).Returns(userAutomations.GetEnumerator());

        _mockDbContext.Setup(x => x.UserAutomations).Returns(mockSet.Object);

        var result = _service.GetAutomations(username);
        Assert.That(result, Is.EqualTo(userAutomation));
    }

    [Test]
    public void DeleteAutomation()
    {
        const string username = "test";
        const int id = 1;

        var dca = new Dca("AAPL", 10, Frequency.Weekly);
        var userAutomation = new UserAutomation(username, [dca]);

        var userAutomations = new List<UserAutomation> { userAutomation }.AsQueryable();

        var mockSet = new Mock<DbSet<UserAutomation>>();
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.Provider).Returns(userAutomations.Provider);
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.Expression).Returns(userAutomations.Expression);
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.ElementType).Returns(userAutomations.ElementType);
        mockSet.As<IQueryable<UserAutomation>>().Setup(m => m.GetEnumerator()).Returns(userAutomations.GetEnumerator());

        _mockDbContext.Setup(x => x.UserAutomations).Returns(mockSet.Object);

        _service.DeleteAutomation(id, username);
    }
}