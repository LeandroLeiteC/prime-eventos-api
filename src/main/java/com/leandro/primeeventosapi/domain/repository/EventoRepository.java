package com.leandro.primeeventosapi.domain.repository;

import com.leandro.primeeventosapi.domain.entity.Evento;
import com.leandro.primeeventosapi.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    Optional<Evento> findByIdAndStatus(Long id, Status status);

    List<Evento> findByCasaDeShowId(Long id);
}
