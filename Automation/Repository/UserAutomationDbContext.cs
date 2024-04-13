using Automation.Model;
using Microsoft.EntityFrameworkCore;

namespace Automation.Repository;

public class UserAutomationDbContext : DbContext
{
    public UserAutomationDbContext(DbContextOptions<UserAutomationDbContext> options) : base(options)
    {
    }

    protected UserAutomationDbContext()
    {
    }

    public virtual DbSet<UserAutomation> UserAutomations { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<UserAutomation>()
            .HasMany(userAutomation => userAutomation.Automations)
            .WithOne(child => child.Parent);

        modelBuilder.Entity<UserAutomation>().ToTable("UserAutomations");
        modelBuilder.Entity<Dca>().ToTable("Dca");
        modelBuilder.Entity<PriceThreshold>().ToTable("PriceThresholdAction");
    }
}