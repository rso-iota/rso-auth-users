package rso.iota.authsvc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import rso.iota.authsvc.common.annotation.Api200;
import rso.iota.authsvc.common.annotation.ControllerCommon;
import rso.iota.authsvc.config.OpenApiConfig;

import java.util.Map;


@ControllerCommon(
        path = "/api/v1/auth",
        tag = "Traefik forward auth",
        description = "Main authentication controller"
)
@AllArgsConstructor
@Slf4j
public class AuthController {
    @GetMapping
    @Operation(
            summary = "Authenticates forward auth request from traefik",
            description = "Authenticates forward auth request from traefik and returns user info"
    )
    @SecurityRequirement(name = OpenApiConfig.OAUTH_SCHEME_NAME)
    @Api200(description = "Check if user is authenticated")
    public ResponseEntity<Void> getAuth() {
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
