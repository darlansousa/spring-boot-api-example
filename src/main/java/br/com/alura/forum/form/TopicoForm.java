package br.com.alura.forum.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;

public class TopicoForm {

	@NotBlank
	@Length(min = 3, max = 10, message = "Por favor, informar um texto de 3 a 10 caracteres.")
	private String titulo;
	@NotNull
	@NotEmpty
	private String mensagem;
	private String nomeCurso;
	
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getNomeCurso() {
		return nomeCurso;
	}
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	
	public Topico convert(CursoRepository cursoRepo) {
		Curso curso = cursoRepo.findByNome(this.nomeCurso);
		return new Topico(this.titulo, this.mensagem, curso);
	}
}
