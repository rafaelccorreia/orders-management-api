FROM maven:3.9.3 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:20-jdk
WORKDIR /usr/src/app
COPY --from=build /app/target/*.jar app.jar

COPY wait-for-it.sh /usr/local/bin/wait-for-it.sh
RUN chmod +x /usr/local/bin/wait-for-it.sh

ENV PORT 8080
EXPOSE $PORT
ENTRYPOINT ["/usr/local/bin/wait-for-it.sh", "rabbitmq:5672", "--", "java", "-jar", "app.jar"]
