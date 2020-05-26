package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.domain.entity.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompraService {

    Compra save(Compra compra);

    Optional<Compra> findByIdAndClienteUsuarioEmail(Long id, String email);

    Page<Compra> findAllByEmail(Pageable pageable, String email);

    void cancelarCompra(Compra compra);
}


