package com.naiki.ecommerce.controllers.config;

import com.naiki.ecommerce.repository.entity.Role;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers("/api/v1/authenticate/**", "/api/v1/gestionCatalogo/productos/",
                                "/api/v1/gestionCatalogo/productos/destacados","/api/v1/gestionCatalogo/productos/categoria/*",
                                "/api/v1/gestionCatalogo/productos/{productoId}/stock").permitAll()
                        .requestMatchers("/api/v1/user/**","/api/v1/gestionProductos/productos","/api/v1/gestionProductos/productos/{productoId}/stock",
                                "/api/v1/gestionProductos/productos/{productoId}/destacado",
                                "/api/v1/gestionProductos/productos/{productoId}").hasAuthority(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore((Filter) jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
