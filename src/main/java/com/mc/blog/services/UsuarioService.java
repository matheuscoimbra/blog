package com.mc.blog.services;

import com.mc.blog.converter.DozerConverter;
import com.mc.blog.domain.Categoria;
import com.mc.blog.domain.Endereco;
import com.mc.blog.domain.Municipio;
import com.mc.blog.domain.Usuario;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.dto.UsuarioDTO;
import com.mc.blog.dto.UsuarioNewDTO;
import com.mc.blog.repositories.EnderecoRepository;
import com.mc.blog.repositories.UsuarioRepository;
import com.mc.blog.security.UserSS;
import com.mc.blog.services.UserService;
import com.mc.blog.services.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	


	
	public Usuario find(Long id) {
		
		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Usuario> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));
	}
	
	@Transactional
	public Usuario insert(Usuario obj) {
		obj.setId(null);
		obj.setIsAtivo(true);
		obj = repo.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}
	
	public Usuario update(Usuario obj) {
		
		return repo.findById(obj.getId())
				.map(g -> {
					Usuario updated = DozerConverter.parseObject(obj, Usuario.class);
					Boolean contem = false;
					contem = obj.getPerfis().stream().anyMatch(perfil -> {
						return perfil.getCod()==Perfil.ADMIN.getCod();
					} );
					if(contem){
						updated.setPerfis(new HashSet<>());
						updated.addPerfil(Perfil.ADMIN);
					}else{
						updated.setPerfis(new HashSet<>());
						updated.addPerfil(Perfil.Usuario);
					}
					Usuario up = repo.save(updated);
					enderecoRepository.save(up.getEnderecos());
					return up;

				}).orElseThrow(() -> new ObjectNotFoundException(
						"Objeto não encontrado! Id: " + obj.getId() ));


	}

	public Page<Usuario> filtro(Pageable pageable, String nome, String cpf) {
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setCpf(cpf);
		usuario.setIsAtivo(true);

		Example<Usuario> example = Example.of(usuario, ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)   // Match string containing pattern
				.withIgnoreCase());
		return repo.findAll(pageable);
	}

	public Optional<Usuario> filtroOnlyOne(String nome, String cpf) {
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setCpf(cpf);
		usuario.setIsAtivo(true);

		Example<Usuario> example = Example.of(usuario, ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)   // Match string containing pattern
				.withIgnoreCase());
		return repo.findOne(example);
	}


	public void delete(Long id) {
		Usuario user = find(id);
		try {
			user.setIsAtivo(false);
			repo.save(user);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
		}
	}
	
	public List<Usuario> findAll() {
		return repo.findAllByIsAtivoTrue();
	}
	
	public Usuario findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
	
		Usuario obj = repo.findByEmailAndIsAtivoTrue(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Usuario.class.getName());
		}
		return obj;
	}
	
	public Page<Usuario> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAllByIsAtivoTrue(pageRequest);
	}
	
	public Usuario fromDTO(UsuarioDTO objDto) {
		return new Usuario(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	public Usuario fromDTO(UsuarioNewDTO objDto) {
		Boolean contem = false;
		Usuario cli = new Usuario(objDto.getId()==null?null:objDto.getId(), objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), pe.encode(objDto.getSenha()));
		if(objDto.getId()!=null) {
			Usuario user = repo.findById(objDto.getId()).get();
			user.getPerfis().forEach(perfil -> System.out.println(perfil.getCod()));
			contem = user.getPerfis().stream().anyMatch(perfil -> {
				return perfil.getCod()==Perfil.ADMIN.getCod();
			} );
			if(!contem && objDto.getAdmin()){
				cli.addPerfil(Perfil.ADMIN);
			}
			if(!contem && !objDto.getAdmin()){
				cli.addPerfil(Perfil.Usuario);
			}

			if(contem && !objDto.getAdmin()){
				cli.setPerfis(new HashSet<>());
				cli.addPerfil(Perfil.Usuario);
			}
		}else {

			if (objDto.getAdmin()!=null) {
				cli.addPerfil(Perfil.ADMIN);
			} else {
				cli.addPerfil(Perfil.Usuario);
			}
		}
		Municipio cid = new Municipio(objDto.getMununicipioId(), null, null);
		Endereco end = null;
		if(objDto.getEnderecoId()==null){
			end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);

		}else{
			end  = enderecoRepository.findById(objDto.getEnderecoId()).get();
			end = new Endereco(end.getId(), objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);

		}
		cli.setEnderecos(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}

		return cli;
	}

	public UsuarioNewDTO toDTO(Usuario o) {

		UsuarioNewDTO usuarioNewDTO = new UsuarioNewDTO();
		//usuarioNewDTO.setSenha(o.getSenha());
		usuarioNewDTO.setNome(o.getNome());
		usuarioNewDTO.setBairro(o.getEnderecos().getBairro());
		usuarioNewDTO.setAdmin(o.getPerfis().contains(Perfil.ADMIN)?true:false);
		usuarioNewDTO.setCep(o.getEnderecos().getCep());
		usuarioNewDTO.setEmail(o.getEmail());
		usuarioNewDTO.setNumero(o.getEnderecos().getNumero());
		usuarioNewDTO.setCpfOuCnpj(o.getCpf());
		usuarioNewDTO.setId(o.getId());
		usuarioNewDTO.setEnderecoId(o.getEnderecos().getId());
		usuarioNewDTO.setLogradouro(o.getEnderecos().getLogradouro());
		usuarioNewDTO.setMununicipioId(o.getEnderecos().getMunicipio().getId());
		usuarioNewDTO.setComplemento(o.getEnderecos().getComplemento());

		return usuarioNewDTO;

	}
	
	private void updateData(Usuario newObj, Usuario obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	

}
