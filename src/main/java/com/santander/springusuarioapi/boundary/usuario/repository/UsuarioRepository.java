package com.santander.springusuarioapi.boundary.usuario.repository;

import com.santander.springusuarioapi.boundary.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findUsuarioByEmail(String email);

}
