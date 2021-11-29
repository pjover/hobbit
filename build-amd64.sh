#!/bin/zsh

./gradlew bootJar
docker build -f Dockerfile-amd64 -t p3r3/hobbit:amd64 .
docker compose -f docker-compose-amd64.yaml up --detach
