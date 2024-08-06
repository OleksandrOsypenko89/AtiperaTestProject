FROM openjdk:21

COPY target/AtiperaTestProject-1.0.jar /app/AtiperaTestProject-1.0.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "AtiperaTestProject-1.0.jar"]