package com.mc.blog.resources;


import com.mc.blog.domain.Usuario;
import com.mc.blog.dto.UsuarioDTO;
import com.mc.blog.dto.UsuarioNewDTO;
import com.mc.blog.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/usuarios")
public class UsuarioResource {

    @Autowired
    private UsuarioService service;

    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public ResponseEntity<UsuarioNewDTO> find(@PathVariable Long id) {
        Usuario obj = service.find(id);
        UsuarioNewDTO dto = service.toDTO(obj);
        return ResponseEntity.ok().body(dto);
    }

    @RequestMapping(value="/email", method= RequestMethod.GET)
    public ResponseEntity<Usuario> find(@RequestParam(value="value") String email) {
        Usuario obj = service.findByEmail(email);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody UsuarioNewDTO objDto) {

        Usuario obj = service.fromDTO(objDto);
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value="/{id}", method= RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody UsuarioNewDTO objDto, @PathVariable Long id) {
        Usuario obj = service.fromDTO(objDto);
        obj.setId(id);
        service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<Usuario> list = service.findAll();
        List<UsuarioDTO> listDto = list.stream().map(obj -> new UsuarioDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value="/page", method= RequestMethod.GET)
    public ResponseEntity<Page<UsuarioDTO>> findPage(
            @RequestParam(value="page", defaultValue="0") Integer page,
            @RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue="nome") String orderBy,
            @RequestParam(value="direction", defaultValue="ASC") String direction) {
        Page<Usuario> list = service.findPage(page, linesPerPage, orderBy, direction);
        Page<UsuarioDTO> listDto = list.map(obj -> new UsuarioDTO(obj));
        return ResponseEntity.ok().body(listDto);
    }

}
