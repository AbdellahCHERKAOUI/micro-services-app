# micro-services-app
Ordre de démarrage : config-service (9999) → discovery-service (8761) → customer (8081) / inventory (8082) → billing (8083) → gateway (8888) → ai-agent (8090).

    mvn clean install -DskipTests
    cd <service> && mvn spring-boot:run

Tests via la gateway (routes dynamiques) :
- GET  http://localhost:8888/customer-service/customers
- GET  http://localhost:8888/inventory-service/products
- GET  http://localhost:8888/billing-service/invoices/1
- POST http://localhost:8888/billing-service/invoices   {"customerId":1,"items":[{"productId":1,"quantity":2}]}
- POST http://localhost:8888/ai-agent-service/chat      {"conversationId":"u1","message":"Montre-moi la facture 1"}

Config dynamique : modifier config-repo/customer-service.properties puis
POST http://localhost:8081/actuator/refresh puis GET http://localhost:8081/customers/config
