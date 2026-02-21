package com.todokanban.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 7 configuration for the Kanban API.
 *
 * <p>Configures the application as a stateless OAuth2 Resource Server
 * that validates JWTs issued by Keycloak. Every {@code /api/v1/**}
 * endpoint requires a valid Bearer token.</p>
 *
 * <p>Design decisions:</p>
 * <ul>
 *   <li>CSRF disabled: not necessary for stateless REST APIs.</li>
 *   <li>No session: each request is independently authenticated via JWT.</li>
 *   <li>Actuator health endpoint permitted without auth for readiness probes.</li>
 *   <li>JWKS URI is auto-discovered from the issuer-uri configured in
 *       {@code application.properties}.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST – no CSRF needed
            .csrf(csrf -> csrf.disable())

            // Never create an HTTP session
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                    // Allow actuator health for container probes (no auth required)
                    .requestMatchers("/actuator/health").permitAll()
                    // Every API route requires a valid JWT
                    .requestMatchers("/api/v1/**").authenticated()
                    // Deny anything else by default
                    .anyRequest().denyAll())

            // Validate JWTs from Keycloak (issuer-uri configured in application.properties)
            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt -> {}));  // JWKS auto-discovered via issuer-uri

        return http.build();
    }
}
