using Automation.Repository;
using Automation.Service;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography;
using Microsoft.AspNetCore.Authentication;

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

// Read public key from file
var publicKeyPath = Path.Combine(Directory.GetCurrentDirectory(), "app.pub");
var publicKeyPem = File.ReadAllText(publicKeyPath);
var publicKey = RSA.Create();
publicKey.ImportFromPem(publicKeyPem);

// Add JWT validation services
builder.Services.AddAuthentication(options =>
    {
        options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
        options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
    })
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = false,
            ValidateAudience = false,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            IssuerSigningKey = new RsaSecurityKey(publicKey)
        };
    });

var app = builder.Build();

app.UseAuthentication();
// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapControllers();
app.UseHttpsRedirection();
app.Run();