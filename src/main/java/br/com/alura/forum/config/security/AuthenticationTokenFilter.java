package br.com.alura.forum.config.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private TokenService service;
    private UsuarioRepository usuarioRepository;

    public AuthenticationTokenFilter(TokenService service, UsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = recuperarToken(request);

        if(service.tokenIsValid(token))
            autenticarClient(token);


        filterChain.doFilter(request, response);
            
        
    }

    private void autenticarClient(String token) {
        Long idUsuario = service.getIdUsuario(token);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if(usuarioOpt.isPresent()){
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuarioOpt.get(), null, usuarioOpt.get().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer "))
            return null;
        
        return token.substring(7, token.length());
    }

    
    
}
