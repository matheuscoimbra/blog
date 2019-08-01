package com.mc.blog.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "categoria_id",nullable = false)
    @NonNull
    private Long id;


    @Column(nullable = false)
    @NonNull
    private String nome;


    @ManyToOne
    @JoinColumn(name="CATEGORIA_PAI_ID")
    private Categoria categoriaPai;




}
