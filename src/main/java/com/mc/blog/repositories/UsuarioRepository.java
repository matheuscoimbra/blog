package com.mc.blog.repositories;

import com.mc.blog.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    @Transactional(readOnly=true)
    Page<Usuario> findAllByNomeIgnoreCaseContainingAAndIsAtivoTrueOrderByNome(String nome, Pageable pageable);

    @Transactional(readOnly=true)
    Usuario findByEmailAAndIsAtivoTrue(String email);

    @Transactional(readOnly=true)
    Page<Usuario> findAllByIsAtivoTrue(Pageable pageable);

}
