package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.api.dto.CasaDeShowDTO;
import com.leandro.primeeventosapi.api.dto.EventoDTO;
import com.leandro.primeeventosapi.api.dto.filtro.CasaDeShowFILTRO;
import com.leandro.primeeventosapi.api.dto.form.CasaDeShowFORM;
import com.leandro.primeeventosapi.domain.entity.CasaDeShow;
import com.leandro.primeeventosapi.domain.entity.Evento;
import com.leandro.primeeventosapi.domain.enums.Status;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.exception.ObjetoNotFoundException;
import com.leandro.primeeventosapi.service.CasaDeShowService;
import com.leandro.primeeventosapi.service.EventoService;
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
    private final EventoService eventoService;

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
        casaDeShow = service.update(casaDeShow);
        return modelMapper.map(casaDeShow, CasaDeShowDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar uma casa de show por um filtro.")
    @ApiResponses({@ApiResponse(code = 200, message = "Casa(s) de Show encontrada(s).")})
    public Page<CasaDeShowDTO> findCasaDeShow(@RequestParam(required = false) String status, @PageableDefault(size = 6) Pageable pageable){

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        CasaDeShow casaDeShow = new CasaDeShow();
        if(status != null){
            status = status.toLowerCase();
            if(status.equals("aberto")){
                casaDeShow = CasaDeShow.builder().status(Status.ABERTO).build();
            }

            if(status.equals("oculto")){
                casaDeShow = CasaDeShow.builder().status(Status.OCULTO).build();
            }
        }

        Example example = Example.of(casaDeShow, matcher);

        Page<CasaDeShow> result = service.findAll(example ,pageable);
        List<CasaDeShowDTO> casas = result.getContent().stream()
                .map(c -> modelMapper.map(c, CasaDeShowDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<CasaDeShowDTO>(casas, pageable, result.getTotalElements());
    }

    @Secured({"ROLE_ADMIN"})
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Ocultar uma Casa de Show")
    @ApiResponses({@ApiResponse(code = 204, message = "Casa de Show Ocultado/Aberto."), @ApiResponse(code = 404, message = "Casa de Show não encontrada."), @ApiResponse(code = 400, message = "Parâmetro não enviado/ enviado incorretamente.")})
    public void updateCasaDeShowStatus(@PathVariable Long id, @RequestParam("status") String status){
        status = status.toLowerCase();
        CasaDeShow casaDeShow = service.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Casa de Show não encontrada."));
        if(status.equals("aberto")){
            service.updateStatus(casaDeShow, Status.ABERTO);
        }else if(status.equals("oculto")){
            service.updateStatus(casaDeShow, Status.OCULTO);
        }else{
            throw new BussinesException("Status inválido.");
        }
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("{id}/eventos")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca os eventos de uma casa de show")
    @ApiResponses({@ApiResponse(code = 200, message = "Eventos encontrados."), @ApiResponse(code = 404, message = "Casa de Show não encontradal.")})
    public List<EventoDTO> getEventosByCasaDeShow(@PathVariable Long id){
        CasaDeShow casaDeShow = service.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Casa de Show não encontrada."));
        List<Evento> eventos = eventoService.findByCasaDeShowId(id);
        return eventos.stream().map(e -> modelMapper.map(e, EventoDTO.class)).collect(Collectors.toList());
    }
}
