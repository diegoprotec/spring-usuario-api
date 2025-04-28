package com.santander.springusuarioapi.boundary.endereco.repository;

import com.santander.springusuarioapi.boundary.endereco.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}

