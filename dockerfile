FROM openjdk:20

WORKDIR /app
COPY ./target/HotelApplication-0.0.1-SNAPSHOT.jar /app

EXPOSE 8082

CMD ["java","-jar","HotelApplication-0.0.1-SNAPSHOT.jar"]