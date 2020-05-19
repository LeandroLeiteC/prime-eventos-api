package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.domain.entity.Compra;
import com.leandro.primeeventosapi.domain.entity.CompraEvento;

import java.util.List;

public interface CompraEventoService {

    List<CompraEvento> saveAll(List<CompraEvento> compraEventos, Compra compra);
}
