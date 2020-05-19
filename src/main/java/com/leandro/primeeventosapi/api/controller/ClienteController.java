package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.api.dto.ClienteDTO;
import com.leandro.primeeventosapi.api.dto.form.ClienteForm;
import com.leandro.primeeventosapi.domain.entity.Cliente;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.service.ClienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Api("API Clientes")
public class ClienteController {

    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final ClienteService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salvar um cliente.")
    @ApiResponses({@ApiResponse(code = 201, message = "Cliente cadastrado."), @ApiResponse(code = 400, message = "Dado(s) inválido(s) ou email já cadastrado.")})
    public ClienteDTO saveCliente(@RequestBody @Valid ClienteForm form){
        if(service.findByEmail(form.getUsuario().getEmail()).isPresent()){
            throw new BussinesException("Email já cadastrado.");
        }

        if(!form.getUsuario().senhaValida()){
            throw new BussinesException("Senhas de cadastro não são iguais.");
        }

        Cliente cliente = modelMapper.map(form, Cliente.class);
        cliente.setId(null);
        cliente.setNome(cliente.getNome().toUpperCase());
        cliente.getUsuario().setEmail(cliente.getUsuario().getEmail().toLowerCase());
        cliente.getUsuario().setPassword(encoder.encode(cliente.getUsuario().getPassword()));
        service.save(cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }
}
