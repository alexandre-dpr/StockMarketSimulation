FROM mcr.microsoft.com/dotnet/aspnet:8.0 as base
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:8.0 as build
WORKDIR /src
COPY ./Automation.csproj ./
RUN dotnet restore ./Automation.csproj
COPY . .

RUN dotnet build . -c Release -o /app

FROM build as publish
RUN dotnet publish . -c Release -o /app

FROM base AS final
WORKDIR /app
COPY --from=publish /app .
COPY app.pub ./
CMD dotnet Automation.dll