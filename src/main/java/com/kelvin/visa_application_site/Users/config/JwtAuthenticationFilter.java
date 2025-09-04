package com.kelvin.visa_application_site.Users.config;

import com.kelvin.visa_application_site.Users.services.JwtServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtServices jwtService;
    private final UserDetailsService customUserDetailsService;   // for users
    private final UserDetailsService adminDetailsService;  // for admins

    public JwtAuthenticationFilter(
            JwtServices jwtService,
            UserDetailsService customUserDetailsService,
            UserDetailsService adminDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.adminDetailsService = adminDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException , IOException { // Todo Explain these exception in details

        final String authHeader = request.getHeader("Authorization");


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try{
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            final String role = jwtService.extractRole(jwt);

            // check if user is already authenticated
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(userEmail != null && authentication == null){
                UserDetails userDetails;
                // pick the right service based on role
                if ("ADMIN".equalsIgnoreCase(role)) {
                    userDetails = this.adminDetailsService.loadUserByUsername(userEmail);
                } else {
                    userDetails = this.customUserDetailsService.loadUserByUsername(userEmail);
                }

                if(jwtService.isTokenValid(jwt, userDetails)){

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( // Todo Ask chat to explain in depth
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

          } catch (Exception e) {
            // 8️⃣ If anything fails (invalid token, etc.), handle exception gracefully
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

}
