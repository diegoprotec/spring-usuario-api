package com.santander.springusuarioapi.boundary.usuario.application;


import com.santander.springusuarioapi.boundary.endereco.domain.Endereco;
import com.santander.springusuarioapi.boundary.endereco.domain.EnderecoMapper;
import com.santander.springusuarioapi.boundary.endereco.domain.EnderecoVo;
import com.santander.springusuarioapi.boundary.usuario.domain.Usuario;
import com.santander.springusuarioapi.boundary.usuario.domain.UsuarioMapper;
import com.santander.springusuarioapi.boundary.usuario.domain.UsuarioVo;
import com.santander.springusuarioapi.boundary.usuario.repository.UsuarioRepository;
import com.santander.springusuarioapi.exception.negocio.NegocioException;
import jakarta.ws.rs.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioApplication implements UsuarioApp {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioApplication.class);

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioApplication(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Cacheable(value = "usuarios", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Override
    public Page<UsuarioVo> buscarTodos(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return usuarioRepository.findAll(pageable).map(UsuarioMapper.INSTANCE::toVo);
        } catch (Exception e) {
            if (e.getCause() instanceof InternalServerErrorException) throw e;
            var mensagem = "USUARIOS: error ao buscar registros";
            LOG.error(mensagem);
            throw new InternalServerErrorException(mensagem);
        }
    }

    @CacheEvict(value = "usuarios", allEntries = true)
    @Override
    public UsuarioVo registrar(EnderecoVo enderecoVo, UsuarioVo usuarioVo) {
        if (emailExists(usuarioVo.email()))
            throw new NegocioException("Email já cadastrado: " + usuarioVo.email());

        try {
            Endereco endereco = EnderecoMapper.INSTANCE.toEntity(enderecoVo);
            Usuario usuario = UsuarioMapper.INSTANCE.toEntity(usuarioVo);
            usuario.setEndereco(endereco);
            usuarioRepository.save(usuario);
            return UsuarioMapper.INSTANCE.toVo(usuario);
        } catch (Exception e) {
            if (e.getCause() instanceof InternalServerErrorException) throw e;
            var mensagem = "USUARIOS: error ao registrar dados";
            LOG.error(mensagem);
            throw new InternalServerErrorException(mensagem);
        }
    }

    private boolean emailExists(String email) {
        try {
            return !usuarioRepository.findUsuarioByEmail(email).isEmpty();
        } catch (Exception e) {
            var mensagem = "Não foi possível realizar validar o usuário";
            LOG.info(mensagem);
            throw new InternalServerErrorException(mensagem);
        }
    }

}
