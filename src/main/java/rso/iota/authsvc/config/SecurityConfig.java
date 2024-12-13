package rso.iota.authsvc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import rso.iota.authsvc.env.KeycloakProperties;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final KeycloakProperties keycloakProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        DefaultBearerTokenResolver resolver = new DefaultBearerTokenResolver();
        // This allows of passing of 'access_token' as a query parameter - used in WebSockets if
        // the client doesn't support Bearer tokens in headers (like browsers)
        resolver.setAllowUriQueryParameter(true);

        return http.csrf(AbstractHttpConfigurer::disable). // Rest API
                formLogin(AbstractHttpConfigurer::disable). // Disable form login
                sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)). // Create stateless session
//                exceptionHandling(eh -> eh.authenticationEntryPoint(restAuthenticationEntryPoint)). // Set entry point
        authorizeHttpRequests(authorize -> authorize.requestMatchers(
        // Actuator - health check
        "/actuator/**",
        // Api docs
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/conf",
        "/static/fonts/**",
        "/sw.js",
        "/favicon.ico").permitAll().anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder()))
                        .bearerTokenResolver(resolver))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // The under laying implementation of JwtDecoder is NimbusJwtDecoder
        return JwtDecoders.fromIssuerLocation(keycloakProperties.getIssuerUrl());
    }
}
