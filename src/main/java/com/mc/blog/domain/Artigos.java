package com.mc.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Artigos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "artigos_id",nullable = false)
    private Long id;

    @Size(min = 2, max = 60, message = "Campo nome com tamanho inválido")
    @NotNull(message = "Campo menssagem obrigatório")
    @Column(nullable = false)
    private String nome;


    @Column()
    @Size(max = 1000)
    private String descricap;

    @Column()
    @Size(max = 1000)
    private String url;

    @Lob
    @Column()
    private byte[] conteudo;

    @JsonIgnoreProperties({"artigos"})
    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @JsonIgnoreProperties({"artigos"})
    @ManyToOne
    @JoinColumn(name="categoria_id")
    private Categoria categoria;


}
