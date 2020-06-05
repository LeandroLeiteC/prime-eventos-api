package com.leandro.primeeventosapi.service.impl;

import com.leandro.primeeventosapi.domain.entity.Compra;
import com.leandro.primeeventosapi.domain.entity.CompraEvento;
import com.leandro.primeeventosapi.domain.enums.StatusCompra;
import com.leandro.primeeventosapi.domain.repository.CompraRepository;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.service.CompraEventoService;
import com.leandro.primeeventosapi.service.CompraService;
import com.leandro.primeeventosapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository repository;
    private final UsuarioService usuarioService;
    private final CompraEventoService compraEventoService;

    @Override
    @Transactional(readOnly = false)
    public Compra save(Compra compra) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        compra.setUsuario(usuarioService.findByEmail(email)
                    .orElseThrow(() -> new BussinesException("Cliente não encontrado.")));

        compra.setId(null);
        compra.setHora(LocalDateTime.now());
        compra.setStatus(StatusCompra.ANDAMENTO);

        List<CompraEvento> compraEventos = compra.getCompraEventos();

        compra.setCompraEventos(null);

        repository.save(compra);

        compraEventos = compraEventoService.saveAll(compraEventos, compra);

        compra.setCompraEventos(compraEventos);
        compra.setTotal(compra.calcularTotal());
        compra.setStatus(StatusCompra.REALIZADO);
        return compra;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Compra> findByIdAndClienteUsuarioEmail(Long id, String email) {
        return repository.findByIdAndUsuarioEmail(id, email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compra> findAllByEmail(String email) {
        return repository.findAllByUsuarioEmail(email);
    }

    @Override
    @Transactional(readOnly = false)
    public void cancelarCompra(Compra compra) {
        compra.getCompraEventos().forEach(compraEvento -> {
            compraEvento.getEvento().setIngressosDisponiveis(compraEvento.getEvento().getIngressosDisponiveis() + compraEvento.getQtdIngresso());
            compraEvento.getEvento().setIngressosVendidos(compraEvento.getEvento().getIngressosVendidos() - compraEvento.getQtdIngresso());
        });
        compra.setStatus(StatusCompra.CANCELADO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraEvento> getCompraEventos(Compra compra) {
        return compra.getCompraEventos();
    }

}
