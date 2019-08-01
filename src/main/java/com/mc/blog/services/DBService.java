package com.mc.blog.services;

import com.mc.blog.domain.Endereco;
import com.mc.blog.domain.Usuario;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.repositories.EnderecoRepository;

import com.mc.blog.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {

	@Autowired
	private BCryptPasswordEncoder pe;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	
	public void instantiateTestDatabase() throws ParseException {
		

		
		Usuario cli1 = new Usuario(new Long(1), "Maria Silva", "nelio.cursos@gmail.com", "36378912377",  pe.encode("123"));
		
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		cli1.addPerfil(Perfil.ADMIN);
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, null);

		
		cli1.getEnderecos().addAll(Arrays.asList(e1));

		usuarioRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1));
		

		

	}
}
