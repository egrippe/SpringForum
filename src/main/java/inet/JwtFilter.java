package inet;

import inet.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter class for handling authorization header.
 *
 * @author Edward Grippe
 */
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // check for authorization header starting with bearer
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        String token = authHeader.substring(7); // The part after "Bearer "

        try {

            // extract claims from token and set as request attributes
            Claims claims = Jwts.parser()
                    .setSigningKey("secretkey")
                    .parseClaimsJws(token)
                    .getBody();

            String user = claims.getSubject();

            request.setAttribute("claims", claims);
            request.setAttribute("userId", claims.get("userId"));
        }
        catch (SignatureException e) {
            throw new ServletException("Invalid token.");
        }

        filterChain.doFilter(request, response);
    }
}
