FROM gradle:8.10.2-jdk17 as build

WORKDIR /app

COPY . .
RUN chmod +x ./gradlew

RUN ./gradlew bootJar

FROM openjdk:17 as runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar .
ENTRYPOINT ["java", "-jar", "*.jar"]