# Hobbit

Hobbit Kotlin Spring boot application for managing a Kindergarten business, initially developed for [Hobbiton](http://www.hobbiton.es) Kindergarten

## How to run

Hobbit will run inside a Kubernetes cluster, alongside with a MongoDB database and Mongo express database administrator.


### Local app with MongoDB on Docker

Also you can run as standard java application, and will connect to MongoDB on localhost:27017

Set program arguments to: `--spring.profiles.active=local`


#### MongoDB on Docker

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


### App docker image

- Build the image with JIB: `.\gradlew jibDockerBuild`


### Run Hobbit on Docker

```shell script
docker run -d \
    --name hobbit-prod \
    --network mongo-network \
    -e "spring.profiles.active=prod" \
    -p 8080:8080 \
    hobbit
```

### Run Hobbit on Kubernetes

We use Hel to build the k8s files and deploy it to the kubernetes cluster.

The database directory location is set as a command line param, in the examples is `/Users/pere/hobbit_db`.

- Examine a chart for possible issues: `helm lint ./chart --values hobbit.yaml`
- Test the template rendering: `helm install --debug --dry-run hobbit ./chart --set db.persitentVolumePath=/Users/pere/hobbit_db`

Run with development database:
- Install the app: `helm install hobbit ./chart --values hobbit.yaml --set db.persitentVolumePath=/Users/pere/hobbit_db`
- Update the app: `helm upgrade --install hobbit ./chart --values hobbit.yaml --set db.persitentVolumePath=/Users/pere/hobbit_db`

Run the latest stable version with production database:
- Install the app: `helm install hobbit ./chart --set appVersion=latest,profile=prod,db.persitentVolumePath=/Users/pere/hobbit_db`

Delete the app from Kubernetes: `helm uninstall hobbit`


## Configure IntelliJ for remote debug

Add new run configuration
- type: Remote
- host: localhost:5005
- Command line args for remote JVM (JDK 5-8): -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
- Use module classpath: hobbit.main