package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepo;
	
	@Autowired
	private CursoRepository cursoRepo;
	
	@GetMapping
	public List<TopicoDto> listar(String nomeCurso) {
		
		if(nomeCurso != null)
			return TopicoDto.convertList(topicoRepo.findByCursoNome(nomeCurso));
		
		return TopicoDto.convertList(topicoRepo.findAll());
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> inserir(@Valid @RequestBody TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.convert(cursoRepo);
		topicoRepo.save(topico);
		URI location = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		
		return ResponseEntity.created(location).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topicoOpt = topicoRepo.findById(id);
		
		if(topicoOpt.isPresent())
			return ResponseEntity.ok(new TopicoDto(topicoOpt.get()));
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> update(@PathVariable Long id, @Valid @RequestBody TopicoForm form) {
		Optional<Topico> topicoOpt = topicoRepo.findById(id);
		
		if(!topicoOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Topico topico = topicoOpt.get();
		topico.setMensagem(form.getMensagem());
		topico.setTitulo(form.getTitulo());
		return ResponseEntity.ok(new TopicoDto(topico));
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable Long id){
		
		Optional<Topico> topicoOpt = topicoRepo.findById(id);
		
		if(!topicoOpt.isPresent())
			return ResponseEntity.notFound().build();
		
		topicoRepo.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
