FROM maven:3.9.5-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
CMD ["mvn", "test"]