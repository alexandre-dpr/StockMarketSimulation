using System.Security.Cryptography;
using System.Text.Json.Serialization;
using Automation.Repository;
using Automation.Service;
using Consul;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Serialization;

var builder = WebApplication.CreateBuilder(args);

// Services
builder.Services.AddControllers()
    .AddJsonOptions(option => { option.JsonSerializerOptions.Converters.Add(new JsonStringEnumConverter()); })
    .AddNewtonsoftJson(
        options =>
        {
            options.SerializerSettings.Converters.Add(new StringEnumConverter(new CamelCaseNamingStrategy()));
        }
    );
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddSingleton<AutomationService>();
builder.Services.AddHostedService<ScheduledTaskService>();
builder.Services.AddRouting(options => options.LowercaseUrls = true);
// TODO Changer URL BDD
builder.Services.AddDbContext<UserAutomationDbContext>(options =>
        options.UseMySql("server=localhost;port=3310;database=your_database;uid=your_user;pwd=your_user_password;",
            new MySqlServerVersion(new Version(8, 3, 0))),
    ServiceLifetime.Singleton
);
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "Automation", Version = "v1" });

    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        Description = "Donner uniquement le token JWT. On rajoute automatiquement Bearer devant.",
        Name = "Authorization",
        In = ParameterLocation.Header,
        Type = SecuritySchemeType.Http,
        Scheme = "bearer"
    });

    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference
                {
                    Type = ReferenceType.SecurityScheme,
                    Id = "Bearer"
                }
            },
            new string[] { }
        }
    });
});
builder.Services.AddCors(options =>
{
    options.AddPolicy("LocalhostPolicy",
        corsBuilder =>
        {
            corsBuilder.WithOrigins("http://localhost:3000")
                .AllowAnyMethod()
                .AllowAnyHeader();
        });
});

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

var consulClient = new ConsulClient();

// Register service with Consul
var registration = new AgentServiceRegistration()
{
    ID = "service-automation-8100",
    Name = "service-automation",
    Address = "localhost",
    Port = 8100,
    Check = new AgentServiceCheck()
    {
        HTTP = "http://localhost:8100/actuator/health", // Health check endpoint
        Interval = TimeSpan.FromSeconds(10) // Check every 10 seconds
    }
};
consulClient.Agent.ServiceRegister(registration);

// Register shutdown hook to deregister service from Consul
AppDomain.CurrentDomain.ProcessExit += (sender, eventArgs) =>
{
    consulClient.Agent.ServiceDeregister("service-automation-8100").Wait();
};


var app = builder.Build();
app.UseCors("LocalhostPolicy");
app.UseAuthentication();
app.UseAuthorization();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapControllers();
app.MapGet("/actuator/health", () =>
    {
        
        return "\"status\": \"UP\"";
    })
    .WithName("GetStatus")
    .WithOpenApi();
app.UseHttpsRedirection();
app.Run();