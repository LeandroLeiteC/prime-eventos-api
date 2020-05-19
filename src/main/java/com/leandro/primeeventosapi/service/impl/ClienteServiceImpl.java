package com.leandro.primeeventosapi.service.impl;

import com.leandro.primeeventosapi.domain.entity.Cliente;
import com.leandro.primeeventosapi.domain.entity.Compra;
import com.leandro.primeeventosapi.domain.repository.ClienteRepository;
import com.leandro.primeeventosapi.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    @Override
    @Transactional(readOnly = false)
    public Cliente save(Cliente cliente) {
        cliente.getUsuario().setAtivo(true);
        cliente.getUsuario().setAdmin(false);
        return repository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll(Example example) {
        List<Cliente> clientes = repository.findAll(example);
        return clientes;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> findByEmail(String email) {
        return repository.findByUsuarioEmail(email);
    }

}
