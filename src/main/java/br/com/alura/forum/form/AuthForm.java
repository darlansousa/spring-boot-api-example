package br.com.alura.forum.form;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthForm {
    
    private String email;
    private String senha;

    public AuthForm(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
    
    public String getEmail() {
        return this.email;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsernamePasswordAuthenticationToken convert() {
        return new UsernamePasswordAuthenticationToken(email, senha);
    }
    
}
