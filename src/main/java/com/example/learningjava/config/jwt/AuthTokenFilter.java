package com.example.learningjava.config.jwt;

import com.example.learningjava.models.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            logger.info("JWT: " + jwt);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                User userDetails = (User) userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            logger.error("Cannot set user authentication: {}", e);
            // Handle JWT exception with custom JSON response
            sendUnauthorizedResponse(response, "Authentication failed due to invalid JWT");
            return; // Stop further processing
        }catch (Exception e) {
            logger.error("Unexpected error: {}", e);
            // Handle any other unexpected exceptions
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
            return; // Stop further processing
        }

        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, message);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        // Set response status
        response.setStatus(statusCode);

        // Set Content-Type header to application/json
        response.setContentType("application/json");

        // Create a JSON-like structure for the error message
        String jsonResponse = String.format("{\"error\": \"%s\", \"status\": %d}", message, statusCode);

        // Write the JSON response to the output stream
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
