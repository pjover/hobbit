# Hobbit

Hobbit Kotlin Spring boot application for managing a Kindergarten business, initially developed for [Hobbiton](http://www.hobbiton.es) Kindergarten

## How to run

Hobbit will run inside a Kubernetes cluster, alongside with a MongoDB database and Mongo express database administrator.


### Local app

But also you can run as standard java application, and will connect to MongoDB on localhost:27017

Set program arguments to: `--spring.profiles.active=local`

### App docker image

- Build the image with JIB: `.\gradlew jibDockerBuild`

### Helm

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