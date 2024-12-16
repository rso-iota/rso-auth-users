package rso.iota.authsvc.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class PublicPathFilterConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String forwardedUri = request.getHeader("X-Forwarded-Uri");
        if (forwardedUri != null) {
            // try to parse the forwarded URI, because we don't want to check query parameters
            forwardedUri = forwardedUri.split("\\?")[0];

            // split by / and check if one of the parts is "public"
            var parts = forwardedUri.split("/");

            for (var part : parts) {
                if (part.equals("public")) {
                    log.info("Allowing a public path request through: {}", forwardedUri);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}