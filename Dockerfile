FROM gradle:8.10.2-jdk17-alpine AS build
WORKDIR /app

COPY . .
RUN gradle --no-daemon build -x test

FROM eclipse-temurin:17-alpine AS runtime
WORKDIR /app
EXPOSE 8080
COPY --from=build /app/build/libs/argos-0.0.1-SNAPSHOT.jar ./
ENTRYPOINT ["java", "-jar", "/app/argos-0.0.1-SNAPSHOT.jar"]
