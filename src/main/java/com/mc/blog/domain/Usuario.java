package com.mc.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mc.blog.domain.enums.Perfil;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@Table
@Entity
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "usuario_id",nullable = false)
	private Long id;

	@Column(nullable = false)
	private String nome;
	
	@Column(unique=true,nullable = false)
	private String email;

	@Column(length=11,nullable = false)
	private String cpf;

	@JsonIgnore
	@Column(nullable = false)
	private String senha;
	
	@OneToMany(mappedBy="usuario", cascade= CascadeType.ALL)
	private List<Endereco> enderecos = new ArrayList<>();

	@OneToMany(mappedBy="usuario")
	private List<Artigos> artigos = new ArrayList<>();
	
	@ElementCollection
	@CollectionTable(name="TELEFONE")
	private Set<String> telefones = new HashSet<>();
	
	@ElementCollection(fetch= FetchType.EAGER)
	@CollectionTable(name="PERFIS")
	private Set<Integer> perfis = new HashSet<>();

	public Usuario(Long id, String nome, String email, String cpf, String senha) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.senha = senha;
	}

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}
	

}
