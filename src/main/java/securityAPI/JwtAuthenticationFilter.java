package securityAPI;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/loginAPI/cadastrar") 
                || path.startsWith("/loginAPI/logar")) {
            filterChain.doFilter(request, response);
            return;
        }

        String proto = request.getHeader("X-Forwarded-Proto");
        boolean isHttps = request.isSecure() || "https".equalsIgnoreCase(proto);

        if (!isHttps) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("HTTPS obrigatório");
            return;
        }

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                var claims = jwtUtil.validarToken(token).getBody();

                String role = claims.get("role", String.class);
                UsernamePasswordAuthenticationToken authentication;

                if ("ROLE_ADMIN".equals(role)) {
                    authentication = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    );
                } else {
                    authentication = new UsernamePasswordAuthenticationToken(
                            claims.get("login", String.class),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido ou expirado");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token ausente");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
