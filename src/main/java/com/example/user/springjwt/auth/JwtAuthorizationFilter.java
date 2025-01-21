package com.example.user.springjwt.auth;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Bỏ qua các endpoint public
            String path = request.getRequestURI();
            if (path.contains("/api/v1/auth/login") || path.contains("/api/v1/auth/register")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Resolve token từ request
            String token = jwtUtil.resolveToken(request);

            if (token != null && jwtUtil.validateToken(token)) {
                // Extract username từ token
                String username = Jwts.parser()
                        .setSigningKey(jwtUtil.getSecretKey().getBytes())
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();

                System.out.println(username + " logged in");

                // Load UserDetails
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Tạo Authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Set security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }
}
