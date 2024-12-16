package rso.iota.authsvc.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rso.iota.authsvc.env.KeycloakAdminProperties;

@Configuration
@AllArgsConstructor
@Slf4j
public class KeycloakAdminConfig {

    private final KeycloakAdminProperties properties;

    @Bean
    Keycloak keycloak() {
        log.info("Creating Keycloak admin client for server: {}, Realm: {}, Client ID: {}",
                properties.serverUrl(),
                properties.realm(),
                properties.clientId());

        return KeycloakBuilder.builder()
                .serverUrl(properties.serverUrl())
                .realm(properties.realm())
                .clientId(properties.clientId())
                .grantType(OAuth2Constants.PASSWORD)
                .username(properties.username())
                .password(properties.password())
                .build();
    }
}
