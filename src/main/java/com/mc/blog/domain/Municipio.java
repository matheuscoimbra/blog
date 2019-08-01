package com.mc.blog.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Municipio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="municipio_id")
	private Long id;
	
	@Column(length = 60, nullable = false)
	private String nome;
	
	@Column(length = 2, nullable = false)
	private String uf;
	
	}
