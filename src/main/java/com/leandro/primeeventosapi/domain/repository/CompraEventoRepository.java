package com.leandro.primeeventosapi.domain.repository;

import com.leandro.primeeventosapi.domain.entity.CompraEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraEventoRepository extends JpaRepository<CompraEvento, Long> {
}
