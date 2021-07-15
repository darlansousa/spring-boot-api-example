package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenDto;
import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.form.AuthForm;

@RestController
@RequestMapping("/auth")
public class AutenticationController {




    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;
    
    @PostMapping
    public ResponseEntity<?> autenticar(@RequestBody @Valid AuthForm form) {
        UsernamePasswordAuthenticationToken tokenLogin = form.convert();

        try {
            Authentication authenticate = authenticationManager.authenticate(tokenLogin);

            String token = tokenService.gerarToken(authenticate);
            
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
            
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
        

        
    }
}
