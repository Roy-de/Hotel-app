FROM openjdk:20
WORKDIR /app
COPY ./target/Hotel-app-0.0.1-SNAPSHOT.jar /app
EXPOSE 8082
CMD ["java", "-jar", "Hotel-app-0.0.1-SNAPSHOT.jar"]