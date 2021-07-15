package br.com.alura.forum.config.security;

public class TokenDto {

    private String token;
    private String authType;

    public TokenDto(String token, String authType) {
        this.token = token;
        this.authType = authType;
    }

    public String getToken() {
        return this.token;
    }

    public String getAuthType() {
        return this.authType;
    }

}
