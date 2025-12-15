package com.cafe.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;

    private Claims claims = null;
    private String userName = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {

        // Allow public endpoints
        if (request.getServletPath().matches("/user/login|/user/forgetpassword|/user/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        userName = null;
        claims = null;

        // If header exists and contains Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);

            // If token is empty, skip filter
            if (token.trim().isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                userName = jwtUtil.extractUsername(token);
                claims = jwtUtil.extractAllClaims(token);
            } catch (Exception e) {
                System.out.println("Invalid Token: " + e.getMessage());
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            // No Authorization header: skip filter safely
            filterChain.doFilter(request, response);
            return;
        }

        // Authenticate user
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customerUserDetailsService.loadUserByUsername(userName);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userName, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    public boolean isAdmin() {
        return claims != null && "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public boolean isUser() {
        return claims != null && "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser() {
        return userName;
    }
}
