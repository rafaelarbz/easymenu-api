package com.easymenu.api.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;


    private final String AUTH_HEADER = "Authorization";
    private final String AUTH_TYPE = "Bearer";


    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        final String token = extractAuthorizationHeader(request);

        if (Objects.isNull(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userToken = jwtUtil.extractUsername(token);

        if (Objects.nonNull(userToken) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userToken);

            if (!jwtUtil.isTokenValid(token, userToken)) {
                throw new UsernameNotFoundException("Failed to authenticate with access token");
            }

            final SecurityContext context = SecurityContextHolder.createEmptyContext();

            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }

    private String extractAuthorizationHeader(HttpServletRequest request) {
        final String headerValue = request.getHeader(AUTH_HEADER);

        if (Objects.isNull(headerValue) || !headerValue.startsWith(AUTH_TYPE)) {
            return null;
        }

        return headerValue.substring(AUTH_TYPE.length()).trim();
    }
}
