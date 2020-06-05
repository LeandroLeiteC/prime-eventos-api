package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.domain.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> findByEmail(String email);

    Usuario save(Usuario usuario);
}
