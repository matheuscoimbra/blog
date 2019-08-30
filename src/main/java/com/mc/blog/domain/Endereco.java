package com.mc.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Endereco implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id",nullable = false)
	private Long id;
	private String logradouro;
	@NotNull(message = "Informe o Nº")
	private String numero;
	private String complemento;
	@NotNull(message = "Informe o nome do bairro")
	private String bairro;
	@NotNull(message = "Informe o CEP")
	private String cep;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	@OneToOne
	@JoinColumn(name="municipio_id")
	private Municipio municipio;

	
	
	
}
