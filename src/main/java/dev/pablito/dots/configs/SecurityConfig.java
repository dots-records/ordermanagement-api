package dev.pablito.dots.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permitir todos los orígenes (puedes personalizarlo si es necesario)
        configuration.setAllowedOrigins(Arrays.asList("*"));
        // Permitir todos los métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("*"));
        // Permitir todos los encabezados
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar configuración a todas las rutas
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll())
        ;
        return http.build();
    }
}