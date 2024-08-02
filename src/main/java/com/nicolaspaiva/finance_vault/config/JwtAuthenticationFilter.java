package com.nicolaspaiva.finance_vault.config;

import com.nicolaspaiva.finance_vault.security.jwt.JwtService;
import com.nicolaspaiva.finance_vault.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static io.micrometer.common.util.StringUtils.isEmpty;
import static io.micrometer.common.util.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.startsWith;

/**
 * The authentication filter is invoked
 * once for every HTTP request, making sure that
 * the user is authenticated and authorised
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserService userService;


    /**
     *
     * @param request HTTP Request
     * @param response HTTP Response
     * @param filterChain the filters to be applied to the request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        final String jwt;

        final String userEmail;

        // Checks whether the string is empty or null and whether
        // it contains "Bearer "
        if(isEmpty(authHeader) || !startsWith(authHeader, "Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        // Checks whether the email exists and if the user is already authenticated
        if(isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

            // If the token is valid, creates a new SecurityContext
            if(jwtService.isTokenValid(jwt, userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(token);

                SecurityContextHolder.setContext(securityContext);
            }
        }

        filterChain.doFilter(request, response);
    }


}
