FROM gradle:jdk17 AS build
WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle --no-daemon build -x test

FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app

EXPOSE 8080

COPY --from=build /app/build/libs/*.jar ./

ENTRYPOINT ["sh", "-c", "java -jar /app/*.jar"]
