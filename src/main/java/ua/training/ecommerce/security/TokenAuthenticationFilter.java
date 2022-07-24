package ua.training.ecommerce.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenHelper;
    private final UserDetailsService userDetailServiceImpl;
    private final String authHeaderName;

    private String getToken(HttpServletRequest request) {

        String authHeader = request.getHeader(authHeaderName);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String error = null;
        String authToken = getToken(request);

        if (authToken != null) {
            String username = tokenHelper.getUsernameFromToken(authToken);
            if (username != null) {
                UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);

                TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                error = "Username from token can't be found in DB.";
            }
        }
        if (error != null) {
            log.error(error);
            SecurityContextHolder.getContext().setAuthentication(new AnonAuthentication());
        }
        chain.doFilter(request, response);
    }

}
