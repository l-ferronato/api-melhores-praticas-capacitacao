package com.minsait.api.controller;

import com.minsait.api.controller.dto.UsuarioResponse;
import com.minsait.api.repository.UsuarioEntity;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.util.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @PreAuthorize("hasAuthority('LEITURA_USUARIO')")
    @GetMapping("/usuario")
    public ResponseEntity<Page<UsuarioResponse>> usuarioFindAll(@RequestParam(required = false) String nome,
                                                                @RequestParam(required = false) String email,
                                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "10") int pageSize) {
        final var usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail(email);
        usuarioEntity.setNome(nome);
        Pageable pageable = PageRequest.of(page, pageSize);

        final Page<UsuarioEntity> usuarioEntityListPage = usuarioRepository.findAll(usuarioEntity.usuarioEntitySpecification(), pageable);
        final  Page<UsuarioResponse> usuarioResponseList = ObjectMapperUtil.mapAll(usuarioEntityListPage, UsuarioResponse.class);
        return ResponseEntity.ok(usuarioResponseList);
    }

    @PreAuthorize("hasAuthority('LEITURA_USUARIO')")
    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id){
        final var usuarioEntity = usuarioRepository.findById(id);
        UsuarioResponse usuarioResponse = new UsuarioResponse();

        if (usuarioEntity.isPresent()){
            usuarioResponse = ObjectMapperUtil.map(usuarioEntity.get(), UsuarioResponse.class);
        }else{
            return new ResponseEntity<>(usuarioResponse, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(usuarioResponse, HttpStatus.OK);
    }

}
