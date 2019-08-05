package com.mc.blog.repositories;

import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	@Transactional(readOnly=true)
	public Page<Categoria> findAll(Pageable pageable);


	List<Categoria> findAllByCategoriaPaiIsNull();

	List<Categoria> findAllByCategoriaPai_Id(Long id);

	Optional<Categoria> findByCategoriaPai_Id(Long id);




}
