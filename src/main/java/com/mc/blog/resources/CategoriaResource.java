package com.mc.blog.resources;


import com.mc.blog.config.ApiPageable;
import com.mc.blog.domain.Categoria;
import com.mc.blog.dto.CategoriaDTO;
import com.mc.blog.services.CategoriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Api(value = "CategoriaResource")
@RestController
@RequestMapping(value="/categoria")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;

	@PostMapping
	public ResponseEntity<Categoria> insert(@Valid @RequestBody CategoriaDTO obj) {
		var cat = service.insert(obj);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(cat);
	}

	@ApiPageable
	@GetMapping
	public ResponseEntity<Page<CategoriaDTO>> findPage( @ApiParam Pageable pageable) {
		var list = service.findPage(pageable);
		return ResponseEntity.ok(list);
	}


	@ApiPageable
	@GetMapping("/list")
	public ResponseEntity<List<CategoriaDTO>> findList( ) {
		var list = service.findAll();
		return ResponseEntity.ok(list);
	}

	@ApiPageable
	@GetMapping(value = "/tree")
	public ResponseEntity<List<CategoriaDTO>> findTree() {
		var list = service.buildTree();
		return ResponseEntity.ok(list);
	}


	@GetMapping(path = {"/{id}"})
	public ResponseEntity<CategoriaDTO> findById(@PathVariable Long id){
		var obj = service.findDTO(id);
		return ResponseEntity.ok().body(obj);
	}


	@ApiOperation(value = "Atualiza uma especifica agencia")
	@PutMapping()
	public Categoria update(@Valid @RequestBody Categoria agencia){
		var dto = service.update(agencia);
		return dto;
	}

	@ApiOperation(value = "Delete uma especifica agencia")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}


}
