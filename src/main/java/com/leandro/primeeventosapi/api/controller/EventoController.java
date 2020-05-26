package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.api.dto.EventoDTO;
import com.leandro.primeeventosapi.api.dto.filtro.EventoFILTRO;
import com.leandro.primeeventosapi.api.dto.form.EventoFORM;
import com.leandro.primeeventosapi.domain.entity.CasaDeShow;
import com.leandro.primeeventosapi.domain.entity.Evento;
import com.leandro.primeeventosapi.domain.enums.StatusEvento;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.exception.ObjetoNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
@Api("API Eventos")
public class EventoController {

    private final ModelMapper modelMapper;
    private final EventoService service;
    private final ServletContext context;

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cadastrar um evento.")
    @ApiResponses({@ApiResponse(code = 201, message = "Evento cadastrado."), @ApiResponse(code = 400, message = "Dado(s) inválido(s).")})
    public EventoDTO saveEvento(@RequestBody @Valid EventoFORM form){
        Evento evento = modelMapper.map(form, Evento.class);
        service.save(evento);
        return modelMapper.map(evento, EventoDTO.class);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("{id}/fotos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Salvar a imagem de um evento (card ou banner)")
    @ApiResponses({@ApiResponse(code = 204, message = "Imagem salva."), @ApiResponse(code = 404, message = "Evento não encontrado."), @ApiResponse(code = 400, message = "Imagem ou tipo não enviados")})
    public void saveEventoFotos(@PathVariable("id") Long id, @RequestParam("foto")MultipartFile foto, @RequestParam("tipo") String tipo){
        service.saveImage(foto, id, tipo);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar um evento pelo id.")
    @ApiResponses({@ApiResponse(code = 200, message = "Evento encontrado."), @ApiResponse(code = 404, message = "Evento não encontrado.")})
    public EventoDTO getEventoById(@PathVariable Long id){
        return service.findByIdAndStatus(id, StatusEvento.ABERTO)
                .map(e -> modelMapper.map(e, EventoDTO.class))
                .orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado."));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Atualizar um evento pelo id.")
    @ApiResponses({@ApiResponse(code = 200, message = "Evento atualizado."), @ApiResponse(code = 404, message = "Evento não encontrado.")})
    public EventoDTO updateEventoById(@PathVariable Long id, @RequestBody EventoFORM form){
        service.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado."));
        Evento evento = modelMapper.map(form, Evento.class);
        evento.setId(id);
        service.update(evento);
        return modelMapper.map(evento, EventoDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar um evento por um filtro.")
    @ApiResponses({@ApiResponse(code = 200, message = "Evento(s) encontrado(s).")})
    public Page<EventoDTO> getAllEventos(@RequestParam(required = false) String status, @PageableDefault(size = 6) Pageable pageable){
        ExampleMatcher matcher = ExampleMatcher
                                    .matching()
                                    .withIgnoreCase()
                                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Evento evento = new Evento();

        if(status != null){
            status = status.toLowerCase();
            if(status.equals("aberto")){
                evento = Evento.builder().status(StatusEvento.ABERTO).build();
            }

            if(status.equals("oculto")){
                evento = Evento.builder().status(StatusEvento.OCULTO).build();
            }
        }

        Example example = Example.of(evento, matcher);

        Page<Evento> result = service.findAll(example ,pageable);
        List<EventoDTO> eventos = result.getContent().stream()
                .map(e -> modelMapper.map(e, EventoDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<EventoDTO>(eventos, pageable, result.getTotalElements());
    }

    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Ocultar um evento")
    @ApiResponses({@ApiResponse(code = 204, message = "Evento Ocultado/Aberto."), @ApiResponse(code = 404, message = "Evento não encontrado."), @ApiResponse(code = 400, message = "Parâmetro não enviado/ enviado incorretamente.")})
    public void updateEventoStatus(@PathVariable Long id, @RequestParam("status") String status){
        status = status.toLowerCase();
        Evento evento = service.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado."));
        if(status.equals("aberto")){
            service.updateStatus(evento, StatusEvento.ABERTO);
        }else if(status.equals("oculto")){
            service.updateStatus(evento, StatusEvento.OCULTO);
        }else{
            throw new BussinesException("Status inválido.");
        }
    }
}
