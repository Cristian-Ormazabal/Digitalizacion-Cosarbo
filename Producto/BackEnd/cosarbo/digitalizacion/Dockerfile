FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
# Compilamos y generamos el .jar
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución 
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]