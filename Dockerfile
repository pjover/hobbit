FROM openjdk:8-jre-alpine
EXPOSE 8080 5005
WORKDIR /opt/target
COPY build/libs/hobbit-5.0.0-SNAPSHOT.jar /opt/target
ENV _JAVA_OPTIONS '-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'
CMD ["java", "-jar", "hobbit-5.0.0-SNAPSHOT.jar"]
