package me.ulises.demo_jwt.config.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.ulises.demo_jwt.config.jwt.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService JWT_SERVICE;
    private final UserDetailsService USER_DETAILS_SERVICE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String TOKEN = getTokenFromRequest(request);
        final String USERNAME;

        if (TOKEN == null) {
            filterChain.doFilter(request, response);
            return;
        }

        USERNAME = JWT_SERVICE.getUsernameFromToken(TOKEN);

        if (USERNAME != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = USER_DETAILS_SERVICE.loadUserByUsername(USERNAME);

            if (JWT_SERVICE.isTokenValid(TOKEN, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String AUTH_HEADER = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(AUTH_HEADER) && AUTH_HEADER.startsWith("Bearer ")) {
            return AUTH_HEADER.substring(7);
        }
        return null;
    }
    
}
