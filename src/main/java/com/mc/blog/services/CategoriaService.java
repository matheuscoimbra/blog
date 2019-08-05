package com.mc.blog.services;

import com.mc.blog.converter.DozerConverter;
import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Categoria;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.dto.CategoriaDTO;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		artigos.setCategoria(categoria);

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

	public List<CategoriaDTO> buildTree(){
		var paiNull = repo.findAllByCategoriaPaiIsNull();
		List<CategoriaDTO> dtos = new ArrayList<>();
		List<CategoriaDTO> tree = new ArrayList<>();
		paiNull.stream().forEach(categoria -> {
			tree.add(findTreeList(toDTO(categoria)));
		});

		return tree;

	}


	public CategoriaDTO findTreeList(CategoriaDTO categorias) {
		List<Categoria> hasChild = repo.findAllByCategoriaPai_Id(categorias.getId());
		if(!hasChild.isEmpty()) {
			hasChild.stream().forEach(categoria -> {
				categorias.setChild(findTreeList(toDTO(categoria)));

			});
		}else {
			return categorias;
		}
		return categorias;
	}


	public CategoriaDTO findTree(CategoriaDTO categorias) {
		Optional<Categoria> hasChild = repo.findByCategoriaPai_Id(categorias.getId());
		if(hasChild.isPresent())
			categorias.setChild(findTree(toDTO(hasChild.get())));
		else
			return categorias;
		return categorias;
	}

	public CategoriaDTO toDTO(Categoria categoria){
		return new CategoriaDTO(categoria.getId(),categoria.getNome(),categoria.getCategoriaPai()==null?null:categoria.getCategoriaPai().getId(),null,categoria.getPath());
	}
}
