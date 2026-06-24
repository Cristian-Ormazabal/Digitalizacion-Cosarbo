package com.cosarbo.digitalizacion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. ENDPOINTS LIBRES (Sin Token)
                .requestMatchers("/api/v1/usuarios/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/servicios-costura/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/reservas/ocupadas").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/reservas/horas-ocupadas").permitAll()
                
                // 2. EXCEPCIONES EXCLUSIVAS PARA CLIENTES
                // Se usa 'hasAnyRole' apuntando a tu rol 'CLIENTE' tal como viene de la base de datos
                .requestMatchers("/api/v1/pedidos/mis-pedidos").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers("/api/v1/reservas/mis-reservas").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers("/api/v1/reservas/agendar").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers("/api/v1/carrito/**").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers("/api/v1/items-carrito/**").hasAnyRole("CLIENTE", "ADMIN")
                
                // 3. RESTRICCIONES ESTRICTAS PARA EL ADMINISTRADOR (Bloquea las raíces globales)
                .requestMatchers("/api/v1/pedidos/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/servicios-costura/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/reservas/**").hasRole("ADMIN")
                
                // 4. CUALQUIER OTRA PETICIÓN
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://cosarbo.netlify.app", "https://cosarbo2.netlify.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}