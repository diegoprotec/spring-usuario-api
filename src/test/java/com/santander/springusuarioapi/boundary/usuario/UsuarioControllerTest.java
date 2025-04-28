package com.santander.springusuarioapi.boundary.usuario;

import com.santander.springusuarioapi.boundary.endereco.application.EnderecoApp;
import com.santander.springusuarioapi.boundary.endereco.domain.EnderecoVo;
import com.santander.springusuarioapi.boundary.usuario.application.UsuarioApp;
import com.santander.springusuarioapi.boundary.usuario.domain.UsuarioVo;
import com.santander.springusuarioapi.boundary.usuario.rest.UsuarioController;
import com.santander.springusuarioapi.boundary.usuario.rest.dto.response.UsuarioResumoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioApp usuarioApp;

    @Mock
    private EnderecoApp enderecoApp;

    @InjectMocks
    private UsuarioController usuarioController;

    private UsuarioVo mockUsuarioVo;
    private EnderecoVo mockEnderecoVo;
    private Page<UsuarioVo> mockUsuarioPage;

    @BeforeEach
    void setUp() {
        // Setup mock EnderecoVo
        mockEnderecoVo = new EnderecoVo(
                1L,
                "Rua Teste",
                "123",
                "Complemento",
                "Bairro",
                "Cidade",
                "Estado",
                "12345-678"
        );

        // Setup mock UsuarioVo
        mockUsuarioVo = new UsuarioVo(
                1L,
                "Test User",
                "test@example.com",
                "123456789",
                "test",
                mockEnderecoVo

        );

        mockUsuarioPage = new PageImpl<>(List.of(mockUsuarioVo));
    }

    @Test
    void listaUsuarios_deveRetornarUsuarios() {
        when(usuarioApp.buscarTodos(0, 10)).thenReturn(mockUsuarioPage);
        Page<UsuarioVo> result = usuarioController.listaUsuarios(0, 10);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mockUsuarioVo, result.getContent().getFirst());
    }

    @Test
    void listaUsuariosResumo_deveRetornarUsuariosResumidos() {
        when(usuarioApp.buscarTodos(0, 10)).thenReturn(mockUsuarioPage);

        Page<UsuarioResumoDto> result = usuarioController.listaUsuariosResumo(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        UsuarioResumoDto dto = result.getContent().getFirst();
        assertEquals(mockUsuarioVo.email(), dto.email());
    }

    @Test
    void registraUsuario_deveRegistarUsuario() {
        when(enderecoApp.registrar(any(EnderecoVo.class))).thenReturn(mockEnderecoVo);
        when(usuarioApp.registrar(eq(mockEnderecoVo), any(UsuarioVo.class))).thenReturn(mockUsuarioVo);

        UsuarioVo result = usuarioController.registraUsuario(mockUsuarioVo);

        assertNotNull(result);
        assertEquals(mockUsuarioVo.id(), result.id());
        assertEquals(mockUsuarioVo.email(), result.email());
    }
}
