package com.mc.blog.services;

import com.mc.blog.converter.DozerConverter;
import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Categoria;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.repositories.ArtigosRepository;
import com.mc.blog.repositories.CategoriaRepository;
import com.mc.blog.security.UserSS;
import com.mc.blog.services.exceptions.AuthorizationException;
import com.mc.blog.services.exceptions.DataIntegrityException;
import com.mc.blog.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	@Autowired
	private ArtigosRepository artigosRepository;

	@Transactional(readOnly = true)
	public Categoria find(Long id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	@Transactional
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		obj = repo.save(obj);
		return obj;
	}
	
	public Categoria update(Categoria obj) {
		return repo.findById(obj.getId())
				.map(g -> {
					Categoria updated = DozerConverter.parseObject(obj, Categoria.class);
					//updated.setTrechos(null);
					Categoria up = repo.save(updated);
					//up.setTrechos(trechoService.findAllByLinha(up.getId()));
					return up;

				}).orElseThrow(() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + obj.getId() ));
	}

	public void delete(Long id) {

		Categoria categoria = find(id);
		Categoria cat = new Categoria();
		cat.setCategoriaPai(categoria);


		Example<Categoria> example = Example.of(cat, ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)   // Match string containing pattern
				.withIgnoreCase());
		List<Categoria> categorias  = repo.findAll(example);
		if(!categorias.isEmpty()){
			throw new DataIntegrityException("Categoria possui subcategorias");
		}

		Artigos artigos = new Artigos();
		artigos.setCategorias(Arrays.asList(categoria));

		Example<Artigos> exampleArt = Example.of(artigos, ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)   // Match string containing pattern
				.withIgnoreCase());
		List<Artigos> artigos1  = artigosRepository.findAll(exampleArt);

		if(artigos1.size()>0){
			throw new DataIntegrityException("Categoria possui artigos relacionados");
		}

		repo.deleteById(id);
	}

	public Page<Categoria> findPage(Pageable pageable) {

		return repo.findAll(pageable);
	}
	

	

}
