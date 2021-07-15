package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "javainuseapi")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepo;

	@Autowired
	private CursoRepository cursoRepo;

	private final String TOPICOS_CONTROLLER = "topicos.controller";

	@GetMapping
	@Cacheable(value = TOPICOS_CONTROLLER)
	public Page<TopicoDto> listar(
		@RequestParam(required = false) 
		String nomeCurso,
		@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) 
		Pageable pageable) {

		if (nomeCurso != null)
			return TopicoDto.convertList(topicoRepo.findByCursoNome(nomeCurso, pageable));

		return TopicoDto.convertList(topicoRepo.findAll(pageable));
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = TOPICOS_CONTROLLER, allEntries = true)
	public ResponseEntity<TopicoDto> inserir(@Valid @RequestBody TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.convert(cursoRepo);
		topicoRepo.save(topico);
		URI location = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

		return ResponseEntity.created(location).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public ResponseEntity<TopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topicoOpt = topicoRepo.findById(id);

		if (topicoOpt.isPresent())
			return ResponseEntity.ok(new TopicoDto(topicoOpt.get()));

		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = TOPICOS_CONTROLLER, allEntries = true)
	public ResponseEntity<TopicoDto> update(@PathVariable Long id, @Valid @RequestBody TopicoForm form) {
		Optional<Topico> topicoOpt = topicoRepo.findById(id);

		if (!topicoOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Topico topico = topicoOpt.get();
		topico.setMensagem(form.getMensagem());
		topico.setTitulo(form.getTitulo());
		return ResponseEntity.ok(new TopicoDto(topico));

	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = TOPICOS_CONTROLLER, allEntries = true)
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Optional<Topico> topicoOpt = topicoRepo.findById(id);

		if (!topicoOpt.isPresent())
			return ResponseEntity.notFound().build();

		topicoRepo.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
