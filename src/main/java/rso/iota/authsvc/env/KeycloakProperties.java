package rso.iota.authsvc.env;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(
        prefix = "svc.keycloak"
)
@Validated
public class KeycloakProperties {
    /**
     * Testing documentation
     */
    @NotNull
    @URL
    private String authServerUrl;

    @NotNull
    private String realm;

    @NotNull
    private String clientId;

    public String getIssuerUrl() {
        return authServerUrl + "/realms/" + realm;
    }
}
