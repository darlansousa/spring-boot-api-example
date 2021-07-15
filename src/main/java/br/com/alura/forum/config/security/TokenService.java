package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    @Value("${forum.jwt.expireIn}")
    private String configExpiresIn;

    @Value("${forum.jwt.secret}")
    private String configSecret;
    
    public String gerarToken(Authentication authentication) {
        Usuario logado = (Usuario) authentication.getPrincipal();
        Date createdAt = new Date();
        Date expiresIn = new Date(createdAt.getTime() + Long.parseLong(this.configExpiresIn));

        return Jwts
        .builder()
        .setIssuer("Aplicação Spring Boot")
        .setSubject(logado.getId().toString())
        .setIssuedAt(createdAt)
        .setExpiration(expiresIn)
        .signWith(SignatureAlgorithm.HS256, this.configSecret)
        .compact();
    }

    public boolean tokenIsValid(String token) {
        try {
            Jwts
            .parser()
            .setSigningKey(this.configSecret)
            .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
           return false;
        }
    }

    public Long getIdUsuario(String token) {
        try {
            return Long.parseLong(Jwts
            .parser()
            .setSigningKey(this.configSecret)
            .parseClaimsJws(token).getBody().getSubject());

        } catch (Exception e) {
           return null;
        }
    }
}
