package com.mc.blog.resources;


import com.mc.blog.config.ApiPageable;
import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Categoria;
import com.mc.blog.dto.ArtigosDTO;
import com.mc.blog.services.ArtigosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Api(value = "ArtigoResource")
@RestController
@RequestMapping(value="/artigo")
public class ArtigoResource {
	
	@Autowired
	private ArtigosService service;

	@PostMapping
	public ResponseEntity<Artigos> insert(@Valid @RequestBody Artigos obj) {
		obj = service.insert(obj);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}

	@ApiPageable
	@GetMapping
	public ResponseEntity<Page<ArtigosDTO>> findPage(@ApiParam Pageable pageable) {
		var list = service.findPage(pageable);
		return ResponseEntity.ok(list);
	}


	@GetMapping(path = {"/{id}"})
	public ResponseEntity<Artigos> findById(@PathVariable Long id){
		var obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	@ApiPageable
	@GetMapping(path = {"/{id}/artigos"})
	public ResponseEntity<Page<Artigos>> findArtigosByCategoria(@PathVariable Long id,Pageable pageable){
		var obj = service.findArtigosByCategoria(id,pageable);
		return ResponseEntity.ok().body(obj);
	}


	@ApiOperation(value = "Atualiza um artigo")
	@PutMapping()
	public Artigos update(@Valid @RequestBody Artigos agencia){
		var dto = service.update(agencia);
		return dto;
	}

	@ApiOperation(value = "Delete um artigo")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@ApiPageable
	@GetMapping(path ={"/"})
	public ResponseEntity<Page<Artigos>> filtro( @RequestParam(value="nome",required = false) String nome,  @RequestParam(value="descricao",required = false) String descricao,
													  @RequestParam(value="nomeUsuario",required = false) String nomeUsuario,@ApiIgnore Pageable pageable) {

		var list = service.filtro(nome,descricao,nomeUsuario,pageable);
		return ResponseEntity.ok(list);
	}


}
