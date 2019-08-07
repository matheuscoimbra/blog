package com.mc.blog.services;

import com.mc.blog.converter.DozerConverter;
import com.mc.blog.domain.*;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.dto.ArtigosDTO;
import com.mc.blog.dto.CategoriaDTO;

import com.mc.blog.repositories.ArtigosRepository;

import com.mc.blog.security.UserSS;
import com.mc.blog.services.exceptions.AuthorizationException;
import com.mc.blog.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArtigosService {
	
	@Autowired
	private ArtigosRepository repo;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private UsuarioService usuarioService;

	private List<Categoria> cats;

	public Artigos find(Long id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Artigos> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Artigos.class.getName()));
	}
	
	@Transactional
	public Artigos insert(Artigos obj) {
		obj.setId(null);
		obj = repo.save(obj);
		return obj;
	}
	
	public Artigos update(Artigos obj) {
		return repo.findById(obj.getId())
				.map(g -> {
					Artigos updated = DozerConverter.parseObject(obj, Artigos.class);
					//updated.setTrechos(null);
					Artigos up = repo.save(updated);
					//up.setTrechos(trechoService.findAllByLinha(up.getId()));
					return up;

				}).orElseThrow(() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + obj.getId() ));
	}

	public void delete(Long id) {
		find(id);

			repo.deleteById(id);
	}

	public Page<Artigos> filtro(String nome,String descricao, String nomeUsuario,Pageable pageable) {
		Artigos artigos = new Artigos();
		artigos.setNome(nome);
		artigos.setDescricao(descricao);
		Optional<Usuario> usuario = usuarioService.filtroOnlyOne(nomeUsuario,null);
		if(!usuario.isPresent()){
			throw new ObjectNotFoundException("Sem resultado");
		}
		artigos.setUsuario(usuario.isPresent()?usuario.get():null);

		Example<Artigos> example = Example.of(artigos, ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)   // Match string containing pattern
				.withIgnoreCase());
		return repo.findAll(example,pageable);
	}

	
	public Page<ArtigosDTO> findPage(Pageable pageable) {

		Page<Artigos> var =  repo.findAll(pageable);
		return var.map(this::convertToArtigosDTO);
	}

	private ArtigosDTO convertToArtigosDTO(Artigos entity) {
		return DozerConverter.parseObject(entity, ArtigosDTO.class);
	}

	private CategoriaDTO convertToCategoriaDTO(Artigos entity) {
		return DozerConverter.parseObject(entity, CategoriaDTO.class);
	}

	public Page<ArtigosDTO> findArtigosByCategoria(Long id, Pageable pageable) {

		cats = new ArrayList<>();
		categoriasPaiFilho(id);
		List<Long> ids = new ArrayList<>();
		cats.forEach(
				categoria -> ids.add(categoria.getId())
		);
		ids.add(id);
		Page<ArtigosDTO> artigosDTOS = repo.findAllByCategoria_IdIn(ids, pageable).map(this::convertToArtigosDTO);
		return artigosDTOS;

	}


	List<Categoria> categoriasPaiFilho(Long id){
		if(cats==null){
			cats = new ArrayList<>();
		}
		List<Categoria> categorias = categoriaService.categoriasByPai(id);
		if(categorias!=null){
			categorias.forEach(
					categoria -> {
						categoriasPaiFilho(categoria.getId());
					}
			);
		}
		cats.addAll(categorias);
		return categorias;
	}

	public List<Categoria> getCats() {
		return cats;
	}

	public void setCats(List<Categoria> cats) {
		this.cats = cats;
	}
}
