package com.santander.springusuarioapi.boundary.usuario.rest;

import com.santander.springusuarioapi.boundary.endereco.application.EnderecoApp;
import com.santander.springusuarioapi.boundary.endereco.domain.EnderecoVo;
import com.santander.springusuarioapi.boundary.usuario.application.UsuarioApp;
import com.santander.springusuarioapi.boundary.usuario.domain.UsuarioVo;
import com.santander.springusuarioapi.boundary.usuario.rest.dto.UsuarioResumoMapper;
import com.santander.springusuarioapi.boundary.usuario.rest.dto.response.UsuarioResumoDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioApp usuarioApp;
    private final EnderecoApp enderecoApp;

    @Autowired
    public UsuarioController(UsuarioApp usuarioApp, EnderecoApp enderecoApp) {
        this.usuarioApp = usuarioApp;
        this.enderecoApp = enderecoApp;
    }

    @GetMapping
    @Produces(MediaType.APPLICATION_JSON)
    public Page<UsuarioVo> listaUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return usuarioApp.buscarTodos(page, size);
    }

    @GetMapping("/lista")
    @Produces(MediaType.APPLICATION_JSON)
    public Page<UsuarioResumoDto> listaUsuariosResumo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return usuarioApp.buscarTodos(page, size)
                .map(UsuarioResumoMapper.INSTANCE::toDto);
    }

    @PostMapping
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UsuarioVo registraUsuario(@Valid @RequestBody UsuarioVo usuarioVo) {
        EnderecoVo enderecoVo = enderecoApp.registrar(usuarioVo.endereco());
        return usuarioApp.registrar(enderecoVo, usuarioVo);
    }
}
