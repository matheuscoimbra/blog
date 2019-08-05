package com.mc.blog.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Data
@Table
@Entity
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "categoria_id",nullable = false)
    private Long id;


    @Column(nullable = false)
    private String nome;


    @ManyToOne
    @JoinColumn(name="CATEGORIA_PAI_ID")
    private Categoria categoriaPai;

    @JsonIgnore
    @OneToMany(mappedBy="categoria")
    private List<Artigos> artigos = new ArrayList<>();


    public Categoria(Long id, String nome, Categoria categoriaPai) {
        this.id = id;
        this.nome = nome;
        this.categoriaPai = categoriaPai;
    }

    public Categoria(Long id) {
        this.id = id;
    }

    public String getPath(){
        String path = "";
        if(categoriaPai==null) {
            path+=getNome();
        }else{
            path = path + (StringUtils.isEmpty(categoriaPai.getPath())?"":categoriaPai.getPath()+" > ")+getNome();
        }
        return path;
    }





}
