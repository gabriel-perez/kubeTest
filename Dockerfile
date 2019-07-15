FROM openjdk:11
ADD build/libs/kraken-0.0.1-SNAPSHOT.jar kraken-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "kraken-0.0.1-SNAPSHOT.jar"]


