# Noir Pizza Backend

Server side of Noir Pizza app. Implements authentication, authorization, API and business logic for cleint side of Noir Pizza. Written in Java + Spring Boot + Hibernate.

## Disclaimer

This is non-profit educational project that was created for improving Java Spring development skills. It is not connected with any organizations called Noir Pizza. All coincidences are accidental. 

## Requirements

 - Java 8+ (pref. Java 17)
 - Maven
 - PostgreSQL
 - Redis
 - Prometheus + Grafana (optional)

## Usage

 - Clone project: `git clone https://github.com/NoirPizza/Backend.git`
 - Change directory: `cd Backend`
 - Create `.env` file and configure it as shown below:
```
REDIS_HOST=<host of your Redis instance, usually 'localhost'>
REDIS_PORT=<port of your Redis instance, usually 6379>

DB_URL=<postgres db url, template: jdbc:postgresql://'host':'port'/'db_name'>
DB_USERNAME=<user of postgres db, must have all access>
DB_PASSWORD=<password of db user>

JWT_ACCESS_TOKEN_LIFETIME=<lifetime of token in ms, for ex. '604800000' (7 days)>
JWT_SECRET_KEY=<base64 encoded string, pref. with length of >= 64 like strong password>
```
 - Run the project: `mvn spring-boot:run`
 - Test endpoints at _**localhost:8080**_

For every info related with endpoints and security implementation check source code.
