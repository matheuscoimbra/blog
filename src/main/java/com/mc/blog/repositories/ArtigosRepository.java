package com.mc.blog.repositories;

import com.mc.blog.domain.Artigos;
import com.mc.blog.domain.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface ArtigosRepository extends JpaRepository<Artigos, Long> {

	@Transactional(readOnly=true)
	public Page<Artigos> findAll(Pageable pageable);

	Page<Artigos> findAllByCategoria_IdIn(List<Long> categoria_id, Pageable pageable);



	
	



}
