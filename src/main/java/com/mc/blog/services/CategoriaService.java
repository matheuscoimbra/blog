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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	@Autowired
	private ArtigosRepository artigosRepository;

	@Transactional(readOnly = true)
	public CategoriaDTO findDTO(Long id) {
		
	/*	UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}*/
		
		Optional<CategoriaDTO> obj = Optional.ofNullable(toDTO(repo.findById(id).get()));
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}


	@Transactional(readOnly = true)
	public Categoria find(Long id) {

	/*	UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}*/

		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	@Transactional
	public Categoria insert(CategoriaDTO obj) {

		return repo.save(fromDTO(obj));
	}
	
	public Categoria update(Categoria obj) {
		return repo.findById(obj.getId())
				.map(g -> {
					Categoria updated = DozerConverter.parseObject(obj, Categoria.class);
					Categoria up = repo.save(updated);
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

	public Page<CategoriaDTO> findPage(Pageable pageable) {
		var page = repo.findAll(pageable);
		return page.map(this::toDTO);

	}

	public static  Map<Long, CategoriaDTO> buildIdMap(Collection<CategoriaDTO> targets){
		Map<Long, CategoriaDTO> result = new HashMap<>();
		if(targets!=null && !targets.isEmpty()){
			final Iterator<CategoriaDTO> iterator = targets.iterator();
			while (iterator.hasNext()){
				final CategoriaDTO next = iterator.next();
				if(next.getId()!=null){
					result.put(next.getId(),next);
				}
			}
		}
		return result;
	}

	public List<Categoria> categoriasByPai(Long id){
	    return repo.findAllByCategoriaPai_Id(id);
    }

	public List<CategoriaDTO> getTree(List<CategoriaDTO> all ){

		final List<CategoriaDTO> result = new ArrayList<>();
		final Map<Long, CategoriaDTO> allMap = buildIdMap(all);
		final Iterator<CategoriaDTO> iterator = all.iterator();
		while (iterator.hasNext()){
			final CategoriaDTO next = iterator.next();
			final Long parentId = next.getParentId();
			if(parentId !=null){
				final CategoriaDTO node = allMap.get(next.getId());
				final CategoriaDTO nodeP = allMap.get(parentId);
				if(nodeP != null){
					if(nodeP.getChildren()==null) {
						nodeP.setChildren(new ArrayList<>());
					}
						nodeP.getChildren().add(node);
				}
			}else{

				result.add(next);
			}
		}
		return result;
	}

	public List<Long> getTreeLongs(List<CategoriaDTO> all ){
		final List<Long> result = new ArrayList<>();
		final Map<Long, CategoriaDTO> allMap = buildIdMap(all);
		final Iterator<CategoriaDTO> iterator = all.iterator();
		while (iterator.hasNext()){
			final CategoriaDTO next = iterator.next();
			final Long parentId = next.getParentId();
			if(parentId !=null){
				final CategoriaDTO node = allMap.get(next.getId());
				final CategoriaDTO nodeP = allMap.get(parentId);
				if(nodeP != null){
					if(nodeP.getChildren()==null) {
						nodeP.setChildren(new ArrayList<>());
					}
					nodeP.getChildren().add(node);
				}
			}else{

				result.add(next.getId());
			}
		}
		return result;
	}

	public List<CategoriaDTO> buildTree(){
		List<CategoriaDTO> dtos = new ArrayList<>();

		repo.findAll().forEach(
				categoria -> {dtos.add(toDTO(categoria));}
		);
		return  getTree(dtos);
	}



	public CategoriaDTO toDTO(Categoria categoria){
		return new CategoriaDTO(categoria.getId(),categoria.getNome(),categoria.getCategoriaPai()==null?null:categoria.getCategoriaPai().getId(),null,categoria.getPath());
	}

	public Categoria fromDTO(CategoriaDTO categoria){
		return Categoria.builder().nome(categoria.getNome()).categoriaPai((categoria.getParentId()==0?null:find(categoria.getParentId()))).id(null).build();
	}


	public List<CategoriaDTO> findAll() {
		List<Categoria> page = repo.findAll();
		return page.stream().map(this::toDTO).collect(Collectors.toList());
	}
}




