package com.mc.blog.services;



import com.mc.blog.domain.Municipio;
import com.mc.blog.repositories.MunicipioRepository;
import com.mc.blog.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService {
	
	@Autowired
	private MunicipioRepository repo;


	public Page<Municipio> findAll(Pageable page) {
		return repo.findAll(page);
	}

	public List<Municipio> findAll() {
		return repo.findAll();
	}

	public Municipio findByIdImp(Long id) {
		return repo.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Nenhum resultado encontrado para este ID"));
	}
	
	public List<Municipio> findByUF(String uf) {
		Municipio exemplo = new Municipio();
		exemplo.setUf(uf);
		return repo.findAll(Example.of(exemplo), Sort.by(Sort.Direction.ASC, "nome"));
	}
}
