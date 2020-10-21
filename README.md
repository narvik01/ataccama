# Ataccama

## Requirements
* Java 11
* Docker

## Usage
Build and run:
```
./gradlew clean build 
./gradlew bootBuildImage && docker-compose -f docker-compose.yml -f docker-compose.dbexamples.yml up
...
docker-compose down --remove-orphans
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

Example calls:
```
curl -v localhost:8080/connections -d@'data/external1.json' -H'Content-Type: application/json'
curl -v localhost:8080/connections
```

## Testing
* Spotbugs static analysis, generates <build/reports/spotbugs/main.xml>
* Spock integration testing
* Testcontainers to run Postgres DB in integration tests
* Example data from https://www.postgresql.org/ftp/projects/pgFoundry/dbsamples/

## Technologies
* Spring Boot
* Spring JPA
* Liquibase - DB initialization
* Schemacrawler - DB introspection and model
* Mapstruct - POJO mappings
* Lombok for fast POC implementation
* Springfox, Swagger - API documentation

## Overview
Controllers in `cz.strangeloop.ataccama.api` provide data loaded by services in `cz.strangeloop.ataccama.service`.
Main database is used with Spring Data JPA. Introspection is performed with plain JDBC and Schemacrawler.
App with its PostgreSQL database can be run with `docker-compose.yml`, example external databases are defined in `docker-compose.dbexamples.yml`.

## TODO/identified issues
* docker image size and performance
* do not send DB passwords in responses
* encrypt passwords in database
* possible SQL injection via table name in data preview, should be validated
* handle more error cases (split UnknownDBException)