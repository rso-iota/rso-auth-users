package rso.iota.authsvc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import rso.iota.authsvc.env.KeycloakProperties;

@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfig {

    private final KeycloakProperties keycloakProperties;

    @Bean
    public ClientRegistration keycloakClientRegistration() {
        return ClientRegistrations.fromIssuerLocation(keycloakProperties.getIssuerUrl())
                .registrationId("keycloak")
                .clientId(keycloakProperties.getClientId())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("openid")
                .userNameAttributeName("preferred_username")
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(keycloakClientRegistration());
    }
}
