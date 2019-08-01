package com.mc.blog.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Artigos implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "artigos_id",nullable = false)
    @NonNull
    private Long id;


    @Column(nullable = false)
    @NonNull
    private String nome;


    @Column()
    @Size(max = 1000)
    private String descricap;

    @Column()
    @Size(max = 1000)
    private String url;

    @Lob
    @Column(nullable = false)
    private byte[] conteudo;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;

    @OneToMany()
    private List<Categoria> categorias = new ArrayList<>();




}
