package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.api.dto.CompraDTO;
import com.leandro.primeeventosapi.domain.entity.Cliente;
import com.leandro.primeeventosapi.domain.entity.Compra;
import com.leandro.primeeventosapi.domain.enums.StatusCompra;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface CompraService {

    Compra save(Compra compra);

    Optional<Compra> findByIdAndClienteUsuarioEmail(Long id, String email);

    Page<Compra> findAllByEmail(Pageable pageable, String email);

    void cancelarCompra(Compra compra);
}


