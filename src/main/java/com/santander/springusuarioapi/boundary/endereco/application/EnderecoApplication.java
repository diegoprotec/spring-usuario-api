package com.santander.springusuarioapi.boundary.endereco.application;

import com.santander.springusuarioapi.boundary.endereco.domain.Endereco;
import com.santander.springusuarioapi.boundary.endereco.domain.EnderecoMapper;
import com.santander.springusuarioapi.boundary.endereco.domain.EnderecoVo;
import com.santander.springusuarioapi.boundary.endereco.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoApplication implements EnderecoApp {

    private static final Logger LOG = LoggerFactory.getLogger(EnderecoApplication.class);

    private final EnderecoRepository enderecoRepository;

    @Autowired
    public EnderecoApplication(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    @Override
    public EnderecoVo registrar(EnderecoVo vo) {
        try {
            Endereco endereco = EnderecoMapper.INSTANCE.toEntity(vo);
            enderecoRepository.save(endereco);
            return EnderecoMapper.INSTANCE.toVo(endereco);
        } catch (Exception e) {
            if (e.getCause() instanceof InternalServerErrorException) throw e;
            var mensagem = "ENDERECOS: error ao registrar dados";
            LOG.info(mensagem);
            throw new InternalServerErrorException(mensagem);
        }

    }


}
