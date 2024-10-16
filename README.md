# Argos

## Development
Argos uses java 17, its package manager is Gradle. The project has been dockerized.

### Recommended commands
Run `$ docker compose build` to *create a new docker image* with the latest changes.
Run `$ docker compose up -d` to *start* the project in the background.
Run `$ docker logs <service-name>` to access the logs of a service.

For more information, review the documentation about [docker compose](https://docs.docker.com/reference/cli/docker/compose/)

Once you have the project up and running you can interact with it at **http://localhost:8080/**. Since the project has a REST API, you can access it at **http://localhost:8080/swagger-ui/index.html**

### Testing
Run `$ ./gradlew test` to run unit tests

