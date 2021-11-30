# Hobbit

Hobbit Kotlin Spring boot application for managing a Kindergarten business, initially developed
for [Hobbiton](http://www.hobbiton.es) Kindergarten

## Configuration file

Copy /src/main/resources/application-local.yaml to application-prod.yaml and configure the Business values

## How to run

### Production profile with docker compose

1. Add a `application-prod.yaml` configuration file to resources directory
2. Build the Hobbit image with Dockerfile and run it with Docker compose:
   - `./build-amd64.sh` for linux, windows and intel Mac
   - `./build-arm64.sh` for Apple Silicon Mac

This will start:

- A MongoDB server on port 27017. To enter into the mongo server run: `docker exec -it hobbit_mongo_server_1 bash`
- A Mongo express database UI at port 8081. The UI is at http://localhost:8081
- Hobbit application at port 8080.The Swagger UI is at http://localhost:8080/swagger-ui.html

### Local app on IntelliJ

You can run as standard java application, and will connect to a running MongoDB on localhost:27017

Set up a SpringBoot running configuration inside IntelliJ:

- Program arguments to: `--spring.profiles.active=local`

1. Stop the production Hobbit application
1. Run the running configuration inside IntelliJ

### Production app on IntelliJ

To run inside IntelliJ with production database, you should:

Set up a SpringBoot running configuration inside IntelliJ:

- Program arguments to: `--spring.profiles.active=prod --db.host=localhost`

1. Stop the production Hobbit application
1. Run the running configuration inside IntelliJ


## Update API definition

Set up a SwaggerCodegen running configuration inside IntelliJ:

- Specification path: $PATH_TO_HOBBIT_PROJECT/API_definition.yaml
- generate files to: $PATH_TO_HOBBIT_PROJECT
- generator
  path: https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/4.3.1/openapi-generator-cli-4.3.1.jar
- Language: kotlin-spring
- Configure generator with JSON file: $PATH_TO_HOBBIT_PROJECT/API_definition_options.json

1. Keep up to date the API definition at `API_definition.yaml`
1. Run the running configuration inside IntelliJ
1. Delete the services
1. Move or copy the changed code to the controllers to controller package
1. Run Analyze > Inspect code on the uncommited files

- Remove redundant qualifier name for all DTO files
- Replace empty class body for all DTO files
- Optimize imports
- Reformat code
  
