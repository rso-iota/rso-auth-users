package rso.iota.authsvc.env;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "svc.keycloak-admin")
public record KeycloakAdminProperties(@NotNull @NotBlank String serverUrl,
                                      @NotNull @NotBlank String realm,
                                      @NotNull @NotBlank String clientId,
                                      @NotNull @NotBlank String username,
                                      @NotNull @NotBlank String password,
                                      @NotNull @NotBlank String frontendClientId) {
}


