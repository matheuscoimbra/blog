package com.mc.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.Date;
import java.util.List;

@Getter @AllArgsConstructor
@Setter
@EqualsAndHashCode @Builder
public class ArtigosDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String nome;

    private String descricao;

    private String url;

    @JsonProperty("autor")
    private String userNome;

    private String conteudo;

    private List<String> tags;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dataCriacao;

}
