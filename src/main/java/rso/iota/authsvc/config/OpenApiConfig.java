package rso.iota.authsvc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rso.iota.authsvc.env.KeycloakProperties;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OpenApiConfig {

    public static final String OAUTH_SCHEME_NAME = "oauth_keycloak";

    private final KeycloakProperties keycloakProperties;

    @Bean
    public OpenAPI openAPI() {
        Info apiInfo = new Info();
        apiInfo.title("Authenticator SVC").description("API docs");

        SecurityScheme securityScheme = new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().implicit(new OAuthFlow().authorizationUrl(
                        keycloakProperties.getIssuerUrl() + "/protocol/openid-connect/auth")))
                .description("Keycloak OAuth2 security scheme");

        // open Api definition with security scheme
        return new OpenAPI().components(new Components().addSecuritySchemes(OAUTH_SCHEME_NAME, securityScheme))
                .info(apiInfo);
    }

}