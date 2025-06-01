package dev.exampleinz.auth_module.infrastructure.adapter.config.filter;

import dev.exampleinz.auth_module.application.service.UserAuthService;
import dev.exampleinz.auth_module.infrastructure.adapter.shared.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthJwtTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserAuthService userAuthService;


    public AuthJwtTokenFilter(JwtUtils jwtUtils, @Lazy UserAuthService userAuthService) {
        this.jwtUtils = jwtUtils;
        this.userAuthService = userAuthService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = parseJwt(request);
            System.out.println("JWT Token from header: " + jwtToken);

            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
                String email = jwtUtils.extractEmailFromToken(jwtToken);
                System.out.println("Extracted email from token: " + email);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userAuthService.loadUserByUsername(email);
                    System.out.println("Loaded UserDetails: " + userDetails.getUsername());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("User authenticated: " + email);
                }
            } else {
                System.out.println("JWT token is null or invalid");
            }
        } catch (Exception e) {
            System.out.println("Cannot set user authentication: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
