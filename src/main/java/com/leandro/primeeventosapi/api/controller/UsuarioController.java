package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.api.dto.CredenciaisDTO;
import com.leandro.primeeventosapi.api.dto.TokenDTO;
import com.leandro.primeeventosapi.domain.entity.Usuario;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.security.jwt.JwtService;
import com.leandro.primeeventosapi.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api("API Usuarios")
public class UsuarioController {

    private final UsuarioServiceImpl service;
    private final JwtService jwtService;

    @PostMapping("auth")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Autenticar um usuario.")
    @ApiResponses({@ApiResponse(code = 200, message = "Usuario autenticado."), @ApiResponse(code = 400, message = "Usuario não pode ser autenticado.")})
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .email(credenciais.getEmail().toLowerCase())
                    .password(credenciais.getPassword())
                    .build();

            UserDetails usuarioAutenticado = service.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getEmail(), token);
        }catch (BussinesException e){
            throw new BussinesException("Email ou senha inválidos.");
        }
    }
}
