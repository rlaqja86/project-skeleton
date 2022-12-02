package com.org.user.config.jwt;

import com.org.user.auth.CustomEmailPasswordToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/api/v1/auth")) {
            filterChain.doFilter(request,response);
        } else {
            String jwtToken = extractJwt(request);
            try {
                if (!StringUtils.isEmpty(jwtToken) && jwtTokenProvider.validate(jwtToken)) {
                    String userId = jwtTokenProvider.getValue(jwtToken);
                    CustomEmailPasswordToken token = new CustomEmailPasswordToken(userId, "", null);
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            } catch (Exception exception) {
                log.error("Failed to add filter to filter chain", exception);
            }
            filterChain.doFilter(request,response);
        }
    }

    private String extractJwt(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.isEmpty(token) && token.startsWith("Bearer ")) {
            return token.substring("Bearer ".length());
        }
        return null;
    }
}
