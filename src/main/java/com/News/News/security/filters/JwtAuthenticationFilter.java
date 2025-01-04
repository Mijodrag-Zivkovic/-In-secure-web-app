package com.News.News.security.filters;

import com.News.News.security.model.CustomAuthenticationToken;
import com.News.News.security.model.CustomUserDetails;
import com.News.News.security.services.JwtService;
import com.News.News.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = jwtService.getUsernameFromToken(jwt);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            Long userId = null;
//            if (userDetails instanceof CustomUserDetails) {
//                userId = ((CustomUserDetails) userDetails).getUserId(); // Extract userId
//            }
//            if (jwtService.isTokenValid(jwt)) {
////                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
////                        userDetails, null, userDetails.getAuthorities());
//                CustomAuthenticationToken authentication = new CustomAuthenticationToken(
//                        userDetails, null, userId, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            if (jwtService.isTokenValid(jwt)) {
                // Extract username, roles, and other data from JWT
                username = jwtService.getUsernameFromToken(jwt);
                List<GrantedAuthority> authorities = new ArrayList<>(jwtService.getAuthoritiesFromToken(jwt)); // Extract roles from the JWT
                Long userId = jwtService.getUserIdFromToken(jwt); // Extract userId from the JWT

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Create CustomAuthenticationToken from JWT data
                    CustomAuthenticationToken authentication = new CustomAuthenticationToken(
                            username, null, userId, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}

