package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.domain.entity.Evento;
import com.leandro.primeeventosapi.domain.enums.Status;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EventoService {

    Evento save(Evento evento);

    Page<Evento> findAll(Example example, Pageable pageable);

    void saveImage(MultipartFile foto, Long id, String tipo);

    void updateStatus(Evento evento, Status status);

    Optional<Evento> findByIdAndStatus(Long id, Status status);

    Optional<Evento> findById(Long id);

    Evento update(Evento evento);

    List<Evento> findByCasaDeShowId(Long id);

    void delete(Evento evento);

    List<Evento> findAllById(List<Long> ids);
}

