FROM openjdk:latest
WORKDIR /app
COPY ./target/Hotel-app-0.0.1-SNAPSHOT.jar /app/Hotel-app-0.0.1-SNAPSHOT.jar
EXPOSE 8082
CMD ["java", "-jar", "Hotel-app-0.0.1-SNAPSHOT.jar"]