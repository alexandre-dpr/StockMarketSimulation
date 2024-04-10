using Automation.Repository;
using Automation.Service;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;

var builder = WebApplication.CreateBuilder(args);

// Services
builder.Services.AddControllers()
    .AddNewtonsoftJson(
        options =>
        {
            options.SerializerSettings.Converters.Add(new StringEnumConverter(new CamelCaseNamingStrategy()));
        }
    );
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<AutomationService>();
builder.Services.AddRouting(options => options.LowercaseUrls = true);
// TODO Changer URL BDD
builder.Services.AddDbContext<UserAutomationDbContext>(options =>
        options.UseMySql("server=localhost;port=3308;database=your_database;uid=your_user;pwd=your_user_password;",
            new MySqlServerVersion(new Version(8, 3, 0))),
    ServiceLifetime.Singleton
);
builder.Services.BuildServiceProvider().GetService<UserAutomationDbContext>().Database.Migrate();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapControllers();
app.UseHttpsRedirection();
app.Run();