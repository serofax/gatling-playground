# Gatling maven

## How to run
Start an application
* [Spring boot example](../spring-boot-example/README.md)
* [Vert.x example](../vertx-example/README.md)

Normal
```bash
$ ./mvnw gatling:test -Dgatling.simulationClass=com.github.serofax.gatling.ProjectPerformanceSimulation
```

With Ip6 Stack disabled
```bash
$ ./mvnw -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv6Addresses=false gatling:test -Dgatling.simulationClass=com.github.serofax.gatling.ProjectPerformanceSimulation
```
