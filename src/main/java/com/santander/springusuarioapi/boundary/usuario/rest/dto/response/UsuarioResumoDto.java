package com.santander.springusuarioapi.boundary.usuario.rest.dto.response;

public record UsuarioResumoDto(
        String nome,
        String email,
        String telefone
) {
}
