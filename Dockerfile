FROM ubuntu:latest
LABEL authors="piyush"

FROM amazoncorretto:17

WORKDIR /app

COPY . /app

RUN ./mvnw package

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/HotelierListingsApplication.jar"]
