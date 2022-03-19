package com.freshplanner.api.security;

import com.freshplanner.api.utility.ApiLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${app.settings.jwt.type}")
    private String jwtType;

    @Autowired
    private JwtManager jwtUtils;

    @Autowired
    private UserAuthorityManager userDetailsService;

    /**
     * Parses the JWT Token for each request and sets the 'Authentication' for the 'SecurityContext'.
     *
     * @param request     from overwritten method
     * @param response    from overwritten method
     * @param filterChain from overwritten method
     * @throws ServletException from overwritten method
     * @throws IOException      from overwritten method
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // parse the token to get raw content
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            // get username with the username from the token
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            // get authentication information from db with the username from the token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // build an authentication object for the request
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // set the authentication into the context of the request
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } // else { no token will be ignored }
        // continue the default filterChain
        filterChain.doFilter(request, response);
    }

    /**
     * Removes the JWT Type from the header. The type prefix should be set in the frontend from the 'AuthInterceptor'.
     *
     * <ol>
     *     <li>Case: JWT with JWT_TYPE as prefix. (from Frontend)</li>
     *     <li>Case: JWT when its raw and without prefix. (from Swagger)</li>
     *     <li>Case: No match found -> null</li>
     * </ol>
     *
     * @return parsed header or null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(jwtType + " ")) {
            return headerAuth.substring(jwtType.length() + 1);
        } else if (StringUtils.hasText(headerAuth)) {
            ApiLogger.warning("JWT Request Filter: Parsed request without JWT_TYPE. Content: " + headerAuth);
            return headerAuth;
        } else {
            return null;
        }
    }
}
