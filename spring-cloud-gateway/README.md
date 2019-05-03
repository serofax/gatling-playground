# Gatling Spring boot application

## Requirements
* JDK 8+
* Docker 
* Docker-compose

## How to start

```bash
$ docker-compose -up -d
$ ./mvnw spring-boot:run
```

## How to stop

```bash
$ docker-compose down -v
```

## Test URLs
With database query
```bash
$ curl http://localhost:8080/project
```

```json
[  
   {  
      "id":1,
      "name":"Example name",
      "shortDescription":"Example short description",
      "description":"Example description",
      "salesRepresentative":"Example sales reprasentive",
      "location":"Example location",
      "numberOfPeople":"3",
      "duration":"Example duration"
   }
]
```
Without database query
```bash
$ curl http://localhost:8080/projectnodb
```

```json
[  
   {  
      "id":null,
      "name":"Example name",
      "shortDescription":"Example short description",
      "description":"Example description",
      "salesRepresentative":"Example sales representative",
      "location":"Example location",
      "numberOfPeople":"3",
      "duration":"Example duration"
   }
]
```

## Server tuning
The application can be customized through the application.properties

```
spring.datasource.hikari.maximum-pool-size=90
```

Defines the hikari connections pool size. The postgres can handle up to 100 connections but you should configure at max approximately 95 connections when you want to spy on the database.

```
server.tomcat.max-threads=200
```
Configures the tomcat worker threads. Default is 200. Tomcat will scale its threads up to the maximum when it gets enough stress.




