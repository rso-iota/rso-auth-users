package rso.iota.authsvc.env;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "svc.auth-forward")
public record AuthControllerProperties(@NotNull Boolean logXForwardedHeaders) {
}

