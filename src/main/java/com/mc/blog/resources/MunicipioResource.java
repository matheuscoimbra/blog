package com.mc.blog.resources;


import com.mc.blog.config.ApiPageable;
import com.mc.blog.domain.Municipio;
import com.mc.blog.services.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping(value="/municipios")
public class MunicipioResource {
	
	@Autowired
	private MunicipioService service;

	@GetMapping()
	public ResponseEntity<List<Municipio>> findAll() {
		List<Municipio> list = service.findAll();
		return ResponseEntity.ok(list);
	}

	@ApiPageable
	@GetMapping(value = "/page")
	public ResponseEntity<Page<Municipio>> findPage(@ApiIgnore Pageable pageable) {
		Page<Municipio> list = service.findAll(pageable);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/{uf}")
	public ResponseEntity<List<Municipio>> findByUF(@PathVariable("uf") String uf) {
		return ResponseEntity.ok(service.findByUF(uf));
	}

}
