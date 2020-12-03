# Hobbit

Hobbit Kotlin Spring boot application for managing a Kindergarten business, initially developed for [Hobbiton](http://www.hobbiton.es) Kindergarten

## How to run

Hobbit will run inside a Kubernetes cluster, alongside with a MongoDB database.

### MongoDB on Docker

For development, you can deploy a Docker container, with local persistence

First, create the network

```shell script
docker network create mongo-network
```

Run the MongoDB server

```shell script
docker run -d \
    --name mongo-server \
    --network mongo-network \
    -v $HOME/mongodb_data:/data/db \
    -p 27017:27017 \
    mongo
```

Run the Mongo express database UI

```shell script
docker run -d \
    --name mongo-express \
    --network mongo-network \
    -p 8081:8081 \
    -e ME_CONFIG_OPTIONS_EDITORTHEME="ambiance" \
    -e ME_CONFIG_MONGODB_SERVER="mongo-server" \
    mongo-express
```

Run the Mongo client to access the database server by command terminal

```shell script
docker run -it --rm \
    --network mongo-network \
     mongo \
     mongo --host mongo-server
```

### Local app

Set program arguments to: `--spring.profiles.active=local`


### App docker image

- Build the image with JIB: `.\gradlew jibDockerBuild`
- Run the image for DEV: `docker container run --network mongo-network -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=dev" hobbit`
- Run the image for PROD: `docker container run --network mongo-network -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=prod" hobbit`

### Helm

- Examine a chart for possible issues: `helm lint ./chart`
- Test the template rendering: `helm install --debug --dry-run hobbit ./chart`

- Install the app (1st time): `helm install hobbit ./chart`
- Update the app: `helm upgrade --install hobbit ./chart`
- Delete the app: `helm uninstall hobbit`

- Run the app inside k8s: `kubectl get svc/hobbit -o wide`