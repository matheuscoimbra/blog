package com.mc.blog.services;

import com.mc.blog.converter.DozerConverter;
import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Endereco;
import com.mc.blog.domain.Municipio;
import com.mc.blog.domain.Usuario;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.dto.UsuarioDTO;
import com.mc.blog.dto.UsuarioNewDTO;
import com.mc.blog.repositories.ArtigosRepository;
import com.mc.blog.repositories.EnderecoRepository;
import com.mc.blog.repositories.UsuarioRepository;
import com.mc.blog.security.UserSS;
import com.mc.blog.services.exceptions.AuthorizationException;
import com.mc.blog.services.exceptions.DataIntegrityException;
import com.mc.blog.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArtigosService {
	
	@Autowired
	private ArtigosRepository repo;


	@Autowired
	private UsuarioService usuarioService;

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
		artigos.setDescricap(descricao);
		Optional<Usuario> usuario = usuarioService.filtroOnlyOne(nomeUsuario,null);
		if(!usuario.isPresent()){
			throw new ObjectNotFoundException("Sem resultado");
		}
		artigos.setUsuario(usuario.isPresent()?usuario.get():null);

		Example<Artigos> example = Example.of(artigos, ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)   // Match string containing pattern
				.withIgnoreCase());
		return repo.findAll(example,pageable);
	}

	
	public Page<Artigos> findPage(Pageable pageable) {

		return repo.findAll(pageable);
	}
	

	

}
