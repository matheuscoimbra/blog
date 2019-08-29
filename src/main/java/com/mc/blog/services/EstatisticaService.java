package com.mc.blog.services;

import com.mc.blog.domain.Estatistica;
import com.mc.blog.repositories.ArtigosRepository;
import com.mc.blog.repositories.CategoriaRepository;
import com.mc.blog.repositories.EstatisticaRepository;
import com.mc.blog.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@EnableAsync
@Component
public class EstatisticaService {

    @Autowired
    private EstatisticaRepository estatisticaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ArtigosRepository artigosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Async
    @Scheduled(fixedRate = 50000000)
    public void scheduleFixedDelayTask() throws InterruptedException {
        Long categorias = categoriaRepository.count();
        Long artigos = artigosRepository.count();
        Long usuarios = usuarioRepository.count();

        Estatistica estatistica = new Estatistica(null,categorias,usuarios,artigos,new Date());

        estatisticaRepository.save(estatistica);
    }

}
