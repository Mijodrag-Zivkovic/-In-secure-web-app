package com.News.News.security.filters;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Profile("secure")
@Component// Apply the filter only in the 'secure' profile
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final List<String> rateLimitedUrls = List.of("/accounts/passwordrecovery"); // List of URLs to rate limit

    private Bucket createNewBucket() {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(5)))) // 5 requests per minute
                .build();
    }

    private Bucket resolveBucket(String ipAddress) {
        return buckets.computeIfAbsent(ipAddress, key -> createNewBucket());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        // Check if the request matches one of the rate-limited URLs
        //System.out.println(requestUri);
        if (rateLimitedUrls.contains(requestUri)) {
            String ipAddress = request.getRemoteAddr();
            Bucket bucket = resolveBucket(ipAddress);

            if (bucket.tryConsume(1)) {
                // Allow the request if within the rate limit
                filterChain.doFilter(request, response);
            } else {
                // Deny the request if rate limit is exceeded
                response.setStatus(429);
                response.getWriter().write("Rate limit exceeded. Try again later.");
            }
        } else {
            // Pass through if the URL is not in the rate-limited list
            filterChain.doFilter(request, response);
        }
    }
}
