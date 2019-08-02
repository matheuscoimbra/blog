package com.mc.blog.services;

import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Categoria;
import com.mc.blog.domain.Endereco;
import com.mc.blog.domain.Usuario;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.repositories.ArtigosRepository;
import com.mc.blog.repositories.CategoriaRepository;
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

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ArtigosRepository artigosRepository;

	
	public void instantiateTestDatabase() throws ParseException {
		

		
		Usuario cli1 = new Usuario(1L, "Maria Silva", "nelio.cursos@gmail.com", "36378912377",  pe.encode("123"));
		
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		cli1.addPerfil(Perfil.ADMIN);
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, null);

		
		cli1.getEnderecos().addAll(Arrays.asList(e1));

		cli1 = usuarioRepository.save(cli1);
		enderecoRepository.saveAll(Arrays.asList(e1));

		Categoria c1 = new Categoria(1L,"JAVA");
		Categoria c2 = new Categoria(2L,"BANCO DE DADOS");

		c1 =categoriaRepository.save(c1);
		c2 =categoriaRepository.save(c2);


		Artigos artigos = new Artigos(null,"artigo 1","descricao do artigo 1","url",null,cli1,null);
		artigos.setCategorias(Arrays.asList(c1,c2));

		artigosRepository.save(artigos);

	}
}
