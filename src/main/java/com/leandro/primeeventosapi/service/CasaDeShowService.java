package com.leandro.primeeventosapi.service;

import com.leandro.primeeventosapi.domain.entity.CasaDeShow;
import com.leandro.primeeventosapi.domain.enums.Status;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CasaDeShowService {

    CasaDeShow save(CasaDeShow form);

    Optional<CasaDeShow> findById(Long id);

    void delete(CasaDeShow casaDeShow);

    Page<CasaDeShow> findAll(Example example, Pageable pageable);

    void updateStatus(CasaDeShow casaDeShow, Status status);

    CasaDeShow update(CasaDeShow form);
}

