package br.com.alura.forum.config;

public class ErrorDto {
    
    private String field;
    private String mensagem;


    public ErrorDto(String field, String mensagem) {
        this.field = field;
        this.mensagem = mensagem;
    }
    
    public String getField() {
        return this.field;
    }

    public String getMensagem() {
        return this.mensagem;
    }

}
