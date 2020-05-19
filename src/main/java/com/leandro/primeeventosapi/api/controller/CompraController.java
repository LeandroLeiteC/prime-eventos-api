package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.api.dto.CompraDTO;
import com.leandro.primeeventosapi.api.dto.form.CompraEventoFORM;
import com.leandro.primeeventosapi.domain.entity.Compra;
import com.leandro.primeeventosapi.domain.entity.CompraEvento;
import com.leandro.primeeventosapi.exception.ObjetoNotFoundException;
import com.leandro.primeeventosapi.service.CompraService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
@Api("API Compras")
public class CompraController{

    private final ModelMapper modelMapper;
    private final CompraService compraService;

    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cadastrar uma compra.")
    @ApiResponses({@ApiResponse(code = 201, message = "Compra cadastrada."), @ApiResponse(code = 400, message = "Dado(s) inválido(s).")})
    public CompraDTO saveCompra(@RequestBody @Valid List<CompraEventoFORM> itens){
        Compra compra = new Compra();
        List<CompraEvento> compraEventos = itens.stream().map(ce -> modelMapper.map(ce, CompraEvento.class)).collect(Collectors.toList());
        compra.setCompraEventos(compraEventos);
        compra = compraService.save(compra);
        CompraDTO dto = modelMapper.map(compra, CompraDTO.class);
        return dto;
    }

    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    @GetMapping("{id}")
    @ApiOperation("Buscar uma compra pelo id.")
    @ApiResponses({@ApiResponse(code = 200, message = "Compra encontrada."), @ApiResponse(code = 404, message = "Compra não encontrada.")})
    public CompraDTO getCompraById(@PathVariable Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Compra compra = compraService.findByIdAndClienteUsuarioEmail(id, email).orElseThrow(() -> new ObjetoNotFoundException("Compra não encontrada."));
        CompraDTO dto = modelMapper.map(compra, CompraDTO.class);
        return dto;
    }

    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    @GetMapping
    @ApiOperation("Buscar uma compra por um filtro.")
    @ApiResponses({@ApiResponse(code = 200, message = "Compra(s) encontrada(s).")})
    public Page<CompraDTO> getAllCompras(@PageableDefault(size = 5) Pageable pageable){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CompraDTO> compras = compraService.findAllByEmail(pageable, email).stream()
                                    .map(c -> modelMapper.map(c, CompraDTO.class))
                                    .collect(Collectors.toList());

        return new PageImpl<CompraDTO>(compras);
    }

    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Cancela uma compra pelo seu id")
    @ApiResponses({@ApiResponse(code = 204, message = "Compra cancelada"), @ApiResponse(code = 404, message = "Compra não encontrada.") })
    public void cancelCompra(@PathVariable("id") Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Compra compra = compraService.findByIdAndClienteUsuarioEmail(id, email)
                            .orElseThrow(() -> new ObjetoNotFoundException("Compra não encontrada."));

        compraService.cancelarCompra(compra);
    }

}
