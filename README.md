# Hobbit

Hobbit Kotlin Spring boot application for managing a Kindergarten business, initially developed for [Hobbiton](http://www.hobbiton.es) Kindergarten

## How to run

### Production profile with docker compose

1. Build the Hobbit image with JIB: `./gradlew jibDockerBuild`
1. Run with docker compose: `docker-compose up`. This will start:
  - A MongoDB server on port 27017. To enter into the mongo server run: `docker exec -it hobbit_mongo_server_1 bash`
  - A Mongo express database UI at port 8081. The UI is at http://localhost:8081
  - Hobbit application at port 8080.The Swagger UI is at http://localhost:8080/swagger-ui.html


### Local app on Docker

Also you can run as standard java application, and will connect to a running MongoDB on localhost:27017

Set up a running configuration inside IntelliJ
- Set program arguments to: `--spring.profiles.active=local`

1. Stop the production Hobbit application
1. Run the running configuration inside IntelliJ
