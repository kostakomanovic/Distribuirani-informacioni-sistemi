FROM maven:alpine
RUN mvn -v
RUN mvn clean package -DskipTests

FROM openjdk:17

EXPOSE 8080

ADD ./target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
