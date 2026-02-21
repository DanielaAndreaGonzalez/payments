package com.testkoa.payments.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String HEALTH_ENDPOINT = "/health";

    @Value("${api.key}")
    private String expectedApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String requestUri = request.getRequestURI();
        
        // Health endpoint no requiere autenticación
        if (HEALTH_ENDPOINT.equals(requestUri)) {
            log.debug("Health endpoint - sin autenticación requerida");
            filterChain.doFilter(request, response);
            return;
        }
        
        // Validar API Key para todos los demás endpoints
        String apiKey = request.getHeader(API_KEY_HEADER);
        
        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("Intento de acceso sin API Key desde: {}", request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"status\": 401, \"message\": \"API Key es requerida. Incluya el header X-API-KEY\"}"
            );
            return;
        }
        
        if (!expectedApiKey.equals(apiKey)) {
            log.warn("Intento de acceso con API Key inválida desde: {}", request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"status\": 401, \"message\": \"API Key inválida\"}"
            );
            return;
        }
        
        log.debug("API Key válida - acceso permitido a: {}", requestUri);
        filterChain.doFilter(request, response);
    }
}
