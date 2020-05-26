package com.leandro.primeeventosapi.api.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioForm {

    @NotEmpty(message = "Campo nome é obrigatório.")
    private String nome;

    @NotEmpty(message = "Campo email é obrigatório.")
    @Email(message = "O email não possue formato válido.")
    private String email;

    @NotEmpty(message = "Campo senha é obrigatório.")
    private String password;

}
