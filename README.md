# Micro-services App : gestion des factures

Application de facturation (clients, produits, factures) basée sur une architecture micro-services, rendue **agentique** grâce à un agent IA conversationnel connecté aux services via **MCP**.

## Architecture

| Service | Port | Rôle |
|---|---|---|
| config-service | 9999 | Configuration centralisée (Spring Cloud Config, dépôt Git) |
| discovery-service | 8761 | Annuaire Eureka |
| gateway-service | 8888 | Spring Cloud Gateway (routage dynamique via Eureka) |
| customer-service | 8081 | Gestion des clients |
| inventory-service | 8082 | Gestion des produits |
| billing-service | 8083 | Factures (OpenFeign vers customer et inventory) |
| ai-agent-service | 8090 | Agent conversationnel (client MCP + Ollama) |
| telegram-bot | 8091 | Client Telegram de l'agent |
| billing-client | 4200 | Client Angular (clients, produits, factures, chat) |

Chaque service métier est aussi un **serveur MCP** : ses opérations (`@Tool`) sont utilisables par l'agent, y compris la création de clients, de produits et de factures.

## Technologies

Java 21, Spring Boot 3.4, Spring Cloud (Gateway, Eureka, Config, OpenFeign), Spring AI (MCP), Ollama (llama3.1), Spring Data JPA + H2, Angular, Docker Compose.

## Lancement avec Docker

Prérequis : Docker Desktop et Ollama (`ollama pull llama3.1`) sur la machine hôte.

```bash
docker compose up --build
```

Avec le bot Telegram (token dans un fichier `.env`) :

```bash
TELEGRAM_BOT_TOKEN=xxxx
docker compose --profile telegram up -d
```

## Accès

- Angular : http://localhost:4200
- Eureka : http://localhost:8761
- Gateway : http://localhost:8888/customer-service/customers

## Exemples de requêtes à l'agent

- « Donne-moi la liste des clients »
- « Crée une facture pour Hassan avec 2 imprimantes »
- « Montre-moi la facture 1 »

## Configuration

Les fichiers de configuration sont dans un dépôt Git séparé, lu par config-service. Après modification et `git push` : `POST /actuator/refresh` sur le service concerné.
