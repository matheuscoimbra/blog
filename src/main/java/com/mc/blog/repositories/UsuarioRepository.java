package com.mc.blog.repositories;

import com.mc.blog.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    @Transactional(readOnly=true)
    Page<Usuario> findAllByNomeIgnoreCaseContainingAndIsAtivoTrueOrderByNome(String nome, Pageable pageable);

    @Transactional(readOnly=true)
    Usuario findByEmailAndIsAtivoTrue(String email);

    @Transactional(readOnly=true)
    Page<Usuario> findAllByIsAtivoTrue(Pageable pageable);

    @Transactional(readOnly=true)
    Usuario findByEmail(String email);

    @Transactional(readOnly=true)
    List<Usuario> findAllByIsAtivoTrue();
}
