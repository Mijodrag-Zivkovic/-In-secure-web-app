package com.News.News.security.configuration;

import com.News.News.security.filters.JwtAuthenticationFilter;
import com.News.News.security.filters.RateLimitingFilter;
import com.News.News.security.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.awt.image.Raster;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor()
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter authFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private final Environment environment;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers("/articles").hasAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .authenticationProvider(authenticationProvider())
                //.addFilterBefore(rateLimitingFilter,UsernamePasswordAuthenticationFilter.class)// Custom authentication provider
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
        if(environment.matchesProfiles("secure"))
        {
            http.addFilterBefore(rateLimitingFilter(),UsernamePasswordAuthenticationFilter.class);
        }
        return http.build();
    }

    @Bean
    @Profile("insecure")
    public PasswordEncoder noPassEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Password encoding
    }

    @Bean
    @Profile("secure")
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @Profile("secure") // Create the filter bean only when the 'secure' profile is active
    public RateLimitingFilter rateLimitingFilter() {
        return new RateLimitingFilter(); // Your RateLimiterFilter implementation
    }
//
//    @Bean
//    @Profile("insecure") // Return null when the 'insecure' profile is active
//    public RateLimitingFilter noOpFilter() {
//        return null;
//    }
}
