package com.mc.blog.dto;

import com.mc.blog.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Data
public class CategoriaDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;

    private String nome;

    private Long parentId;

    private List<CategoriaDTO> children = new ArrayList<>();

    private String path;


    public CategoriaDTO() {
        this.id = 0L;
        this.nome = "";
        this.parentId = 0L;
        this.children = new ArrayList<CategoriaDTO>();
        this.path = "";
    }
}
