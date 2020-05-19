package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.domain.entity.Cliente;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente save(Cliente cliente);

    List<Cliente> findAll(Example example);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByEmail(String email);

}
