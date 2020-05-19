package com.leandro.primeeventosapi.api.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteForm {

    @NotEmpty(message = "Campo nome é obrigatório.")
    private String nome;

    @Valid
    private UsuarioForm usuario;
}
