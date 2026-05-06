package com.hotel.sistemahotel.security;

import com.hotel.sistemahotel.shared.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { //intercepta cada request

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtUtil.extractClaims(token);

            UUID usuarioId  = UUID.fromString(claims.getSubject());
            UUID empresaId  = UUID.fromString(claims.get("empresaId", String.class));
            String sucursalStr = claims.get("sucursalId", String.class);
            UUID sucursalId = sucursalStr != null ? UUID.fromString(sucursalStr) : null;
            String rol      = claims.get("rol", String.class);

            // Cargar contexto del tenant en el hilo actual
            TenantContext.setUsuarioId(usuarioId);
            TenantContext.setEmpresaId(empresaId);
            TenantContext.setSucursalId(sucursalId);

            // Registrar autenticación en Spring Security
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            usuarioId,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            TenantContext.clear();
        }

        filterChain.doFilter(request, response);

        // Limpiar el contexto al terminar el request
        TenantContext.clear();
    }
}