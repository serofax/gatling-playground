# Gatling Vertx application

## Requirements
* JDK 8+
* Docker 
* Docker-compose

## How to start

```bash
$ docker-compose -up -d
$ ./gradlew run
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

## Vertx tuning
```yaml
vertx:
    workerPoolSize: 20
    internalBlockingPoolSize: 20
    eventLoopPoolSize: 8
```
See [VertxOptions](https://vertx.io/docs/apidocs/io/vertx/core/VertxOptions.html)

