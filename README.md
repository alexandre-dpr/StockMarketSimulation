# StockMarketSimulation

Site de simulation boursière. Permet aux utilisateurs de pratiquer et tester leurs
stratégies d'investissement sans prendre de risques financiers.

### Installation service Authentification

Générer les clés RSA :

```bash
./generatekeys.sh
```

### Installation service Bourse

Générer la table ticker en exécutant le script ticker.sql
> bourse/src/main/resources/ticker.sql

Définir le token pour accéder à l'API FMI dans application.properties \
/!\ Attention à ne pas push le token