package rso.iota.authsvc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import rso.iota.authsvc.common.annotation.Api200;
import rso.iota.authsvc.common.annotation.ControllerCommon;
import rso.iota.authsvc.config.OpenApiConfig;
import rso.iota.authsvc.env.AuthControllerProperties;

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
    @Api200(
            description = "Check if user is authenticated",
            headers = {@Header(
                    name = "X-User-Sub",
                    description = "User's sub claim"
            ), @Header(
                    name = "X-User-Email",
                    description = "User's email claim"
            )}
    )
    public ResponseEntity<Void> getAuth(@RequestHeader(
            value = "X-Forwarded-Method",
            required = false
    ) String forwardedMethod, @RequestHeader(
            value = "X-Forwarded-Proto",
            required = false
    ) String forwardedProto, @RequestHeader(
            value = "X-Forwarded-Host",
            required = false
    ) String forwardedHost, @RequestHeader(
            value = "X-Forwarded-Uri"
    ) String forwardedUri, @RequestHeader(
            value = "X-Forwarded-For",
            required = false
    ) String forwardedFor) {

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

            return ResponseEntity.ok().header("X-User-Sub", sub).header("X-User-Email", email).build();
        } else {
            log.error("Authentication is not an instance of JwtAuthenticationToken");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}


