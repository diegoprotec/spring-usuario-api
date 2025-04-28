package com.santander.springusuarioapi.boundary.usuario.application;

import com.santander.springusuarioapi.boundary.endereco.domain.EnderecoVo;
import com.santander.springusuarioapi.boundary.usuario.domain.UsuarioVo;
import org.springframework.data.domain.Page;

public interface UsuarioApp {

    Page<UsuarioVo> buscarTodos(int page, int size);

    UsuarioVo registrar(EnderecoVo enderecoVo, UsuarioVo usuarioVo);

}
