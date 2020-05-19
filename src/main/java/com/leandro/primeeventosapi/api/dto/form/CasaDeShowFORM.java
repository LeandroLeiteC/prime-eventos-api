package com.leandro.primeeventosapi.api.dto.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasaDeShowFORM {

    @NotEmpty(message = "Campo nome é obrigatório.")
    private String nome;

    @NotEmpty(message = "Campo cep é obrigatório.")
    @Pattern(regexp = "^\\d{5}[-]\\d{3}$", message = "Cep deve ser no formato '#####-###'.")
    private String cep;

    @NotEmpty(message = "Campo uf é obrigatório")
    @Size(min = 2, max = 2, message = "Uf deve ser no formato 'XX'.")
    private String uf;

    @NotEmpty(message = "Campo cidade é obrigatório.")
    private String cidade;

    @NotEmpty(message = "Campo bairro é obrigatório.")
    private String bairro;

    @NotEmpty(message = "Campo rua é obrigatório.")
    private String rua;

    @NotNull(message = "Campo número é obrigatório.")
    private Integer numero;

    @NotNull(message = "Campo limite de pessoas é obrigatório.")
    private Integer limitePessoas;

    @NotEmpty(message = "Campo telefone é obrigatório")
    @Pattern(regexp = "(\\(\\d{2}\\)\\s)(\\d{4,5}\\-\\d{4})",
            message = "Telefone deve ver no formato '(##) 9####-####'")
    private String telefone;

}
