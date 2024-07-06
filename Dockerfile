FROM openjdk:17
EXPOSE 8080
ADD build/libs/artemyev-middle-service-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar", "/app/app.jar"]