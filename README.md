# Auth service

This service is responsible for user authentication of users in RSO cluster. 
It receives requests from the Traeefik Ingress Controller and verifiies them againts the Keycloak server.
It attaches the "X-User-Sub" in the request headers, which is the user's UUID in the Keycloak server.

In addition it also handles an admin connection to the Keycloak server, which is used to query online users.

## Development

This server is a Spring Boot application. You need to have the following installed:

- Java 21
- Gradle >7
- Intellij IDEA

Create an `appplication-local.yaml` file with the following content:

```yaml
svc:
  keycloak-admin:
    server-url: 
    client-id: 
    realm: 
    username: 
    password: 
    frontend-client-id: 
```

In the `applicaion.yaml` lookup other common properties, such as the server port, Swagger configuration, etc.