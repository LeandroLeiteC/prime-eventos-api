package com.leandro.primeeventosapi.api.dto;

import com.leandro.primeeventosapi.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasaDeShowDTO {

    private Long id;
    private String nome;
    private String cep;
    private String uf;
    private String cidade;
    private String bairro;
    private String rua;
    private Integer numero;
    private Integer limitePessoas;
    private String telefone;
    private Status status;
}
