package com.turnos.reservas.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImplement userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    public SecurityConfig(UserDetailsServiceImplement userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profesionales/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/servicios/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/profesionales/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/profesionales/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/profesionales/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/servicios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/servicios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/servicios/**").hasRole("ADMIN")

                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/clientes/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

}
