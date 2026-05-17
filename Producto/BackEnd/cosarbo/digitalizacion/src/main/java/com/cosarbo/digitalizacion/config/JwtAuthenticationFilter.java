package com.cosarbo.digitalizacion.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Imprime la ruta que se está intentando consultar
        System.out.println("🔍 INTERCEPTANDO RUTA: " + request.getRequestURI());

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ ALERTA: Petición sin cabecera Bearer Token.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Se intenta leer ambos para ver cuál viene con datos (DEBUG)
            String rolEspañol = jwtService.extractClaim(jwt, claims -> claims.get("rol", String.class));
            String roleIngles = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));
            
            System.out.println("📬 ANALIZANDO TOKEN DE: " + userEmail);
            System.out.println("👉 Valor de 'rol' (es): " + rolEspañol);
            System.out.println("👉 Valor de 'role' (en): " + roleIngles);

            if (jwtService.isTokenValid(jwt, userEmail)) {
                // Se usa el que no venga nulo de forma dinámica para salvar la petición
                String rolFinal = (rolEspañol != null) ? rolEspañol : roleIngles;

                if (rolFinal != null) {
                    String autoridadLimpia = "ROLE_" + rolFinal.toUpperCase().trim();
                    System.out.println("✅ AUTORIDAD INYECTADA A SPRING: " + autoridadLimpia);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail, null, List.of(new SimpleGrantedAuthority(autoridadLimpia))
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("❌ ERROR CRÍTICO: El token no traía ni 'rol' ni 'role'. Viene vacío.");
                }
            } else {
                System.out.println("❌ ERROR: El token no es válido o ya expiró.");
            }
        }
        filterChain.doFilter(request, response);
    }
}