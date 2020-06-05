package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.domain.entity.Compra;
import com.leandro.primeeventosapi.domain.entity.CompraEvento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CompraService {

    Compra save(Compra compra);

    Optional<Compra> findByIdAndClienteUsuarioEmail(Long id, String email);

    List<Compra> findAllByEmail(String email);

    void cancelarCompra(Compra compra);

    List<CompraEvento> getCompraEventos(Compra id);
}


