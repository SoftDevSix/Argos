FROM gradle:8.10.2-jdk17 as build

WORKDIR /app

COPY . .

RUN gradle bootjar

FROM openjdk:17
WORKDIR /app
COPY --from=build /app/build/libs/argos-v1.jar .
ENTRYPOINT ["java","-jar","argos-v1.jar"]

