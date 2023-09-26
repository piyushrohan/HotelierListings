# Hotelier Listings API

Welcome to the Hotelier Listings API project! This project provides an API for managing accommodations, locations, and bookings.

## Table of Contents
- [Description](#description)
- [Dependencies](#dependencies)
- [Build and Run](#build-and-run)
- [Run with Docker Compose](#run-with-docker-compose)

## Description
This Hotelier Listings API offers a comprehensive set of endpoints designed for tasks such as creating, retrieving, updating, and deleting accommodations, locations, and bookings. When considering the operation of this application, it is advisable to utilize docker-compose for seamless execution.

Upon launching the application, three vital tables—namely, `locations`, `accommodations`, and `bookings`—are automatically generated within the Postgres database. Notably, each table is furnished with an `id` column serving as a _primary key_, incremented serially. Additionally, both the `location_id` and `accommodation_id` columns act as _foreign keys_ originating from the `locations` and `accommodations` tables, respectively.

A noteworthy procedural element emerges when dealing with accommodations. Specifically, a successful entry in the `accommodations` table necessitates the prior existence of a corresponding `location_id`, ensuring a reference to an extant location record.

During the booking process, the utilization of the `accommodation_id` exclusively is recommended. Successful booking directly results in a decrement of availability by one for the chosen accommodation. Conversely, deletion of a booking triggers an increment of availability by one.

In summation, this Hotelier Listings API streamlines and simplifies the management of accommodations, locations, and bookings through a well-structured and efficient approach. Utilizing docker-compose further enhances the operational fluidity of this dynamic application.

## Dependencies
This project is built using Spring Boot and Kotlin. Key dependencies include:
- Spring Boot: 3.1.2
- Kotlin: 1.9.0
- OpenAPI Generator: 6.6.0
- PostgreSQL: 42.6.0
- jOOQ: 3.18.6
- Liquibase: 4.23.1

## Build and Run
1. **Clone the Repository:** Clone this repository to your local environment.

2. **Build** Maven is used as build tool, execute the following command:
```bash
mvn clean install
```

3. **Run with Docker Compose** Use Docker Compose to run this Application, execute the following command:
```bash
docker-compose up
```
`ports: 8080 for application and 5432 for postgres`
4. You may skip step 2 and run docker compose directly :)

## API Documentation & Postman Collections

The API documentation is automatically generated from the OpenAPI specification. You can find the documentation here: `src/main/resources/static/hotelier-listings-openapi-spec.yml`.
Additionally, you can use the postman collections in `src/main/resources/static/HotelierListings.postman_collection.json` to explore and test the API endpoints.