package com.mc.blog.resources;

import com.mc.blog.domain.Estatistica;
import com.mc.blog.repositories.EstatisticaRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "EstatisticaResource")
@RestController
@RequestMapping("/estatisticas")
public class EstatisticaResource {

    @Autowired
    private EstatisticaRepository repository;


    @GetMapping
    public ResponseEntity<Estatistica> get(){
        return ResponseEntity.ok(repository.findTopByOrderByIdDesc());
    }
}
