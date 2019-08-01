package com.mc.blog.repositories;

import com.mc.blog.domain.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

	@Transactional(readOnly=true)
	public Page<Municipio> findAll(Pageable pageable);




}
