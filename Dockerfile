FROM maven:3.9-eclipse-temurin-24

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

RUN mkdir -p /app/logs


EXPOSE 8080


CMD ["java", "-jar", "target/testLangChain4j-1.0-SNAPSHOT.jar"]
