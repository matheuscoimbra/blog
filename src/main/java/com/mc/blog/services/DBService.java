package com.mc.blog.services;

import com.mc.blog.domain.*;
import com.mc.blog.domain.enums.Perfil;
import com.mc.blog.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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

	@Autowired
	private MunicipioRepository municipioRepository;

	
	public void instantiateTestDatabase() throws ParseException {

		
		Usuario cli1 = new Usuario(1L, "Maria Silva", "teste@teste.com", "36378912377",  pe.encode("123"));
		
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		cli1.addPerfil(Perfil.ADMIN);

		cli1.setIsAtivo(true);

		Municipio m1 = new Municipio(1L,"São Luis", "sl");
		Municipio m2 = new Municipio(2L,"São Bento", "sb");
		Municipio m3 = new Municipio(3L,"Rosário", "ro");
		Municipio m4 = new Municipio(4L,"Raposa", "ra");
		 municipioRepository.saveAll(Arrays.asList(m1,m2,m3,m4));


		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, m1);

		
		cli1.setEnderecos(e1);

		cli1 = usuarioRepository.save(cli1);
		enderecoRepository.saveAll(Arrays.asList(e1));

		Categoria c0 = new Categoria(1L,"PROGRAMACAO",null);
		c0 =categoriaRepository.save(c0);
		Categoria c1 = new Categoria(2L,"JAVA",null);
		c1 =categoriaRepository.save(c1);
		Categoria c2 = new Categoria(3L,"BANCO DE DADOS",c1);
		c2 =categoriaRepository.save(c2);
		Categoria c3 = new Categoria(4L,"SRING BOOT",c2);
		c3 =categoriaRepository.save(c3);
		Categoria c4 = new Categoria(5L,"AWS",c3);
		c4 =categoriaRepository.save(c4);
		Categoria c5 = new Categoria(6L,"ORACLE",c1);
		c5 =categoriaRepository.save(c5);


		Artigos artigos = new Artigos(null,"artigo 1","descricao do artigo 1",null,"Conteudo",cli1,null,new Date(),null);
		artigos.setCategoria(c1);

        Artigos artigos2 = new Artigos(null,"artigo 2","descricao do artigo 1",null,"Conteudo",cli1,null,new Date(),null);
        artigos2.setCategoria(c5);

        Artigos artigos3 = new Artigos(null,"artigo 3","descricao do artigo 3","url","Conteudo",cli1,null,new Date(),null);
        artigos3.setCategoria(c3);

        Artigos artigos4 = new Artigos(null,"artigo 4","descricao do artigo 4","url","Conteudo",cli1,null,new Date(),null);
        artigos4.setCategoria(c4);

		Artigos artigos5 = new Artigos(null,"artigo 5","descricao do artigo 5","url","Conteudo",cli1,null,new Date(),null);
		artigos5.setCategoria(c2);

		artigosRepository.saveAll(Arrays.asList(artigos,artigos2,artigos3,artigos4,artigos5));

	}
}
