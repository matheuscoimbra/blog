package com.mc.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import com.mc.blog.domain.Categoria;
import com.mc.blog.domain.Usuario;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data @Builder
@EqualsAndHashCode
public class ArtigoNewDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;

    @NotEmpty(message="Preenchimento obrigatório do campo nome")
    private String nome;

    private String descricao;

    private String url;

    private String conteudo;

    @NotNull(message = "Autor não informado")
    private Long usuario;

    @NotNull(message = "Categoria não informada")
    private Long categoria;

}