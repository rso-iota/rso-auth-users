package rso.iota.authsvc.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserSessionRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import rso.iota.authsvc.common.annotation.Api200;
import rso.iota.authsvc.common.annotation.ControllerCommon;
import rso.iota.authsvc.dto.OutUserLookup;
import rso.iota.authsvc.env.KeycloakAdminProperties;

import java.util.List;

@ControllerCommon(
        path = "/user",
        tag = "Users",
        description = "Operations related to users"
)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final Keycloak keycloak;

    private final KeycloakAdminProperties keycloakAdminProperties;

    private ClientResource clientResource;

    @PostConstruct
    public void init() {
        ClientRepresentation client = keycloak.realm(keycloakAdminProperties.frontendRealm())
                .clients()
                .findByClientId(keycloakAdminProperties.frontendClientId())
                .getFirst();
        clientResource = keycloak.realm(keycloakAdminProperties.frontendRealm()).clients().get(client.getId());
    }

    @GetMapping("/online")
    @Operation(
            summary = "Get currently online users",
            description = "Returns a list of users that are currently online"
    )
    @Api200(description = "List of online users")
    public ResponseEntity<List<OutUserLookup>> getOnlineUsers(@RequestHeader(value = "X-User-Sub") String userId) {
        List<UserSessionRepresentation> sessions = clientResource.getUserSessions(0, 20);

        return ResponseEntity.ok(sessions.stream()
                .map(session -> OutUserLookup.builder()
                        .id(session.getUserId())
                        .username(session.getUsername())
                        .me(session.getUserId().equals(userId))
                        .build())
                .toList());
    }
}
