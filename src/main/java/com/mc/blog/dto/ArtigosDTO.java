package com.mc.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import com.mc.blog.domain.Categoria;
import com.mc.blog.domain.Usuario;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class ArtigosDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    private String nome;

    private String descricao;

    private String url;

    @JsonProperty("autor")
    private String userNome;

    private String conteudo;

}
