package util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * @author @gui_gomes_18
 */

@Component
public class JwtUtil {

    private final Key key;
    private static final long EXPIRATION_TIME = 1000 * 60 * 15;
    private static final long EXPIRATION_TIME_SESSAO_LONGA = 1000L * 60 * 60 * 24 * 7;
    private static final long EXPIRATION_TIME_USUARIO_TESTE = 1000 * 60 * 120; 
    private static final long EXPIRATION_TIME_MESTRE = 1000 * 60 * 60 * 24;
    
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String gerarTokenSessao(Long userId, String login, String tipoDeUsuario ) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("login", login)
                .claim("tipoDeUsuario", tipoDeUsuario)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String gerarTokenDeSessaoLonga(Long userId, String login, String tipoDeUsuario) {
    	return Jwts.builder()
    			.setSubject(userId.toString())
    			.claim("login", login)
    			.claim("tipoDeUsuario", tipoDeUsuario)
    			.setIssuedAt(new Date())
    			.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_SESSAO_LONGA))
    			.signWith(key, SignatureAlgorithm.HS256)
    			.compact();
    }
    
    public String gerarTokenUsuarioTeste(Long userId, String login) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("login", login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_USUARIO_TESTE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String gerarTokenMestre() {
        return Jwts.builder()
                .setSubject("mestre")
                .claim("role", "ROLE_ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MESTRE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validarToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String getLoginDoToken(String token) {
        return validarToken(token).getBody().get("login", String.class);
    }
}