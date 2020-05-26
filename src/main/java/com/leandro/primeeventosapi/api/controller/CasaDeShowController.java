package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.api.dto.CasaDeShowDTO;
import com.leandro.primeeventosapi.api.dto.filtro.CasaDeShowFILTRO;
import com.leandro.primeeventosapi.api.dto.form.CasaDeShowFORM;
import com.leandro.primeeventosapi.domain.entity.CasaDeShow;
import com.leandro.primeeventosapi.domain.entity.Evento;
import com.leandro.primeeventosapi.exception.ObjetoNotFoundException;
import com.leandro.primeeventosapi.service.CasaDeShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/casasdeshow")
@RequiredArgsConstructor
@Api("API Casas de Show")
public class CasaDeShowController {

    private final CasaDeShowService service;
    private final ModelMapper modelMapper;

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cadastrar uma casa de show.")
    @ApiResponses({@ApiResponse(code = 201, message = "Casa de Show cadastrada."), @ApiResponse(code = 400, message = "Dado(s) inválido(s).")})
    public CasaDeShowDTO saveCasaDeShow(@RequestBody @Valid CasaDeShowFORM form){
        CasaDeShow casaDeShow = modelMapper.map(form, CasaDeShow.class);
        casaDeShow.setId(null);
        casaDeShow = service.save(casaDeShow);
        return modelMapper.map(casaDeShow, CasaDeShowDTO.class);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar uma casa de show pelo id.")
    @ApiResponses({@ApiResponse(code = 200, message = "Encontrou a casa de show."), @ApiResponse(code = 404, message = "Casa de show não encontrada.")})
    public CasaDeShowDTO getCasaDeShowById(@PathVariable Long id){
        CasaDeShow casaDeShow = service.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Casa de show não encontrada."));
        return modelMapper.map(casaDeShow, CasaDeShowDTO.class);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletar uma casa de show pelo id.")
    @ApiResponses({@ApiResponse(code = 204, message = "Casa de show deletada."), @ApiResponse(code = 204, message = "Casa de show não encontrada.")})
    public void deleteCasaDeShowById(@PathVariable Long id){
        CasaDeShow casaDeShow = service.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Casa de show não encontrada."));
        service.delete(casaDeShow);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Atualizar uma casa de show.")
    @ApiResponses({@ApiResponse(code = 200, message = "Casa de show atualizada."), @ApiResponse(code = 404, message = "Casa de show não encontrada.")})
    public CasaDeShowDTO updateCasaDeShowById(@PathVariable Long id, @RequestBody CasaDeShowFORM form){
        service.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Casa de show não encontrada."));
        CasaDeShow casaDeShow = modelMapper.map(form, CasaDeShow.class);
        casaDeShow.setId(id);
        casaDeShow = service.save(casaDeShow);
        return modelMapper.map(casaDeShow, CasaDeShowDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar uma casa de show por um filtro.")
    @ApiResponses({@ApiResponse(code = 200, message = "Casa(s) de Show encontrada(s).")})
    public Page<CasaDeShowDTO> findCasaDeShow(CasaDeShowFILTRO filtro, @PageableDefault(size = 6) Pageable pageable){

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        CasaDeShow casaDeShow = CasaDeShow.builder()
                .nome(filtro.getNome())
                .bairro(filtro.getBairro())
                .cidade(filtro.getCidade())
                .uf(filtro.getUf())
                .build();

        Example example = Example.of(casaDeShow, matcher);

        Page<CasaDeShow> result = service.findAll(example ,pageable);
        List<CasaDeShowDTO> casas = result.getContent().stream()
                .map(c -> modelMapper.map(c, CasaDeShowDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<CasaDeShowDTO>(casas, pageable, result.getTotalElements());
    }
}
