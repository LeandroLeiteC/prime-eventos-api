package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.api.dto.CredenciaisDTO;
import com.leandro.primeeventosapi.api.dto.TokenDTO;
import com.leandro.primeeventosapi.api.dto.UsuarioDTO;
import com.leandro.primeeventosapi.api.dto.form.UsuarioForm;
import com.leandro.primeeventosapi.domain.entity.Usuario;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.exception.ObjetoNotFoundException;
import com.leandro.primeeventosapi.security.jwt.JwtService;
import com.leandro.primeeventosapi.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Api("API Usuarios")
public class UsuarioController {

    private final UsuarioServiceImpl service;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @PostMapping("auth")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Autenticar um usuario.")
    @ApiResponses({@ApiResponse(code = 200, message = "Usuario autenticado."), @ApiResponse(code = 400, message = "Usuario não pode ser autenticado.")})
    public ResponseEntity autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .email(credenciais.getEmail().toLowerCase())
                    .password(credenciais.getPassword())
                    .build();

            UserDetails usuarioAutenticado = service.autenticar(usuario);
            usuario = service.findByEmail(usuario.getEmail()).get();
            String token = jwtService.gerarToken(usuario);

            HttpHeaders header = new HttpHeaders();
            header.set("x-access-token", token);

            return new ResponseEntity(header, HttpStatus.OK);
        }catch (BussinesException e){
            throw new BussinesException("Email ou senha inválidos.");
        }
    }

    @PostMapping("usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salvar um usuário.")
    @ApiResponses({@ApiResponse(code = 201, message = "Usuario cadastrado."), @ApiResponse(code = 400, message = "Dado(s) inválido(s) ou email já cadastrado.")})
    public UsuarioDTO saveUsuario(@RequestBody @Valid UsuarioForm form){
        if(service.findByEmail(form.getEmail()).isPresent()){
            throw new BussinesException("Email já cadastrado.");
        }

        Usuario usuario = modelMapper.map(form, Usuario.class);
        usuario.setId(null);
        usuario.setAdmin(false);
        usuario.setNome(usuario.getNome().toUpperCase());
        usuario.setEmail(usuario.getEmail().toLowerCase());
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        service.save(usuario);
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("usuarios/admin")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salvar um usuário admin.")
    @ApiResponses({@ApiResponse(code = 201, message = "Usuario admin cadastrado."), @ApiResponse(code = 400, message = "Dado(s) inválido(s) ou email já cadastrado.")})
    public UsuarioDTO saveUsuarioAdmin(@RequestBody @Valid UsuarioForm form){
        if(service.findByEmail(form.getEmail()).isPresent()){
            throw new BussinesException("Email já cadastrado.");
        }

        Usuario usuario = modelMapper.map(form, Usuario.class);
        usuario.setId(null);
        usuario.setAdmin(true);
        usuario.setNome(usuario.getNome().toUpperCase());
        usuario.setEmail(usuario.getEmail().toLowerCase());
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        service.save(usuario);
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @GetMapping("usuarios")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Buscar um usuario pelo email.")
    @ApiResponses({@ApiResponse(code = 200, message = "Usuario já cadastrado."), @ApiResponse(code = 404, message = "Usuario não cadastrado.")})
    public boolean usuarioExistsByEmail(@RequestParam String email){
        Optional<Usuario> usuario = service.findByEmail(email);
        if(usuario.isPresent()){
            return true;
        }else{
            return false;
        }
    }
}
