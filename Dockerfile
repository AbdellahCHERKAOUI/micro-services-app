FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -DskipTests package

FROM eclipse-temurin:21-jre
ARG MODULE
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /app/${MODULE}/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]