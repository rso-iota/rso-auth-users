package rso.iota.authsvc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import rso.iota.authsvc.common.annotation.Api200;
import rso.iota.authsvc.common.annotation.ControllerCommon;
import rso.iota.authsvc.config.OpenApiConfig;

import java.util.Map;


@ControllerCommon(
        path = "/auth",
        tag = "Traefik forward auth",
        description = "Main authentication controller for traefik forward auth"
)

@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthControllerProperties properties;

    @GetMapping
    @Operation(
            summary = "Authenticates forward auth request from traefik",
            description = """
                    Uses the access token stored in the "Authorization" header to check if the user is authenticated.
                    The check if performed to the Keycloak server.
                    
                    If th e "X-Forwarded-Uri" includes "public" the request is allowed through without authentication.
                    This is used for the public paths, such as the OpenAPI documentation.
                    """
    )
    @SecurityRequirement(name = OpenApiConfig.OAUTH_SCHEME_NAME)
    @Api200(description = "Check if user is authenticated")
    public ResponseEntity<Void> getAuth(@RequestHeader("X-Forwarded-Method") String forwardedMethod,
                                        @RequestHeader("X-Forwarded-Proto") String forwardedProto,
                                        @RequestHeader("X-Forwarded-Host") String forwardedHost,
                                        @RequestHeader("X-Forwarded-Uri") String forwardedUri,
                                        @RequestHeader("X-Forwarded-For") String forwardedFor) {

        if (properties.logXForwardedHeaders()) {
            log.info("Forwarded headers: method: {}, proto: {}, host: {}, uri: {}, for: {}",
                    forwardedMethod,
                    forwardedProto,
                    forwardedHost,
                    forwardedUri,
                    forwardedFor);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            Map<String, Object> claims = jwtToken.getTokenAttributes();
            String email = (String) claims.get("email");
            String sub = (String) claims.get("sub");

            log.info("User (sub: {}, email: {}) authenticated", sub, email);

            return ResponseEntity.ok().header("X-User-Info", String.format("sub: %s, email: %s", sub, email)).build();
        } else {
            log.error("Authentication is not an instance of JwtAuthenticationToken");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

@Validated
@ConfigurationProperties(prefix = "svc.auth-forward")
record AuthControllerProperties(@NotNull Boolean logXForwardedHeaders) {
}


