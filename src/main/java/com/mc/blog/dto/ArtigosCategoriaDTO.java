package com.mc.blog.dto;

import com.mc.blog.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtigosCategoriaDTO {

    Page<ArtigosDTO> artigosDTOPage;
    Categoria categoria;
}
