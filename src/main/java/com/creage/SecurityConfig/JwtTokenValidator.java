package com.creage.SecurityConfig;

import java.io.IOException;
import javax.crypto.SecretKey;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader(JWT_CONSTANT.JWT_HEADER);
        String requestURI = request.getRequestURI();

        boolean requiresAuthentication = requestURI.startsWith("/api/v1") && !isPublicEndpoint(requestURI);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));
                int writeaccess = Integer.parseInt(String.valueOf(claims.get("writeaccess")));

                UserDetails userDetails = new User(
                        email,
                        "",
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                );

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                ((AbstractAuthenticationToken) authentication).setDetails(writeaccess);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                handleErrorResponse(response, "Token Expired");
                return;
            } catch (JwtException | IllegalArgumentException e) {
                handleErrorResponse(response, "Invalid Token");
                return;
            }
        } else if (requiresAuthentication) {
            handleErrorResponse(response, "Missing or invalid authorization header");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.matches("^/api/v1/auth/(signup|login|ui-login|forget-password|reset-password)$");
    }

    private void handleErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{"
                + "\"result\": false,"
                + "\"data\": null,"
                + "\"apiKey\": null,"
                + "\"message\": \"" + message + "\""
                + "}");
    }
}
