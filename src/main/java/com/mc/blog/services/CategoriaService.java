package com.mc.blog.services;

import com.mc.blog.converter.DozerConverter;
import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Categoria;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.repositories.ArtigosRepository;
import com.mc.blog.repositories.CategoriaRepository;
import com.mc.blog.security.UserSS;
import com.mc.blog.services.exceptions.AuthorizationException;
import com.mc.blog.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

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
		find(id);
		repo.deleteById(id);
	}

	public Page<Categoria> findPage(Pageable pageable) {

		return repo.findAll(pageable);
	}
	

	

}
