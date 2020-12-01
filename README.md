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

--spring.profiles.active=dev