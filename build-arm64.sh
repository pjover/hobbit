#!/bin/zsh

./gradlew bootJar
docker build -f Dockerfile-arm64 -t p3r3/hobbit:arm64 .
docker compose -f docker-compose-arm64.yaml up --detach
