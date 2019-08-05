package com.mc.blog.dto;

import com.mc.blog.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoriaDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;

    private String nome;

    private Long parentId;

    private CategoriaDTO child;

    private String path;



}
