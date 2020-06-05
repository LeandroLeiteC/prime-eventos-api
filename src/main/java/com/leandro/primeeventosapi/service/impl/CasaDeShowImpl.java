package com.leandro.primeeventosapi.service.impl;

import com.leandro.primeeventosapi.domain.entity.CasaDeShow;
import com.leandro.primeeventosapi.domain.enums.Status;
import com.leandro.primeeventosapi.domain.repository.CasaDeShowRepository;
import com.leandro.primeeventosapi.service.CasaDeShowService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CasaDeShowImpl implements CasaDeShowService {

    private final CasaDeShowRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = false)
    public CasaDeShow save(CasaDeShow casaDeShow) {
        casaDeShow.setStatus(Status.ABERTO);
        return repository.save(casaDeShow);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CasaDeShow> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(CasaDeShow casaDeShow) {
        repository.delete(casaDeShow);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CasaDeShow> findAll(Example example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CasaDeShow casaDeShow, Status status) {
        casaDeShow.setStatus(status);
    }

    @Override
    public CasaDeShow update(CasaDeShow form) {
        return repository.save(form);
    }
}
