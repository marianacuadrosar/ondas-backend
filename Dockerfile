# ===============================
# 1. BUILD STAGE (Maven)
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Primero copiamos el pom.xml (mejor cache)
COPY pom.xml .

# Luego descargamos dependencias sin copiar el código aún
RUN mvn dependency:go-offline

# Ahora copiamos el código fuente
COPY src ./src

# Construimos el jar
RUN mvn clean package -DskipTests


# ===============================
# 2. RUNTIME STAGE (Java)
# ===============================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiamos el JAR construido en el primer stage
COPY --from=build /app/target/*.jar app.jar

# Puerto de la app
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
