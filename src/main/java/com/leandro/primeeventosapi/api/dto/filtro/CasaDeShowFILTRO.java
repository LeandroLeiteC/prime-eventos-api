package com.leandro.primeeventosapi.api.dto.filtro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CasaDeShowFILTRO {

    private String nome;
    private String cidade;
    private String uf;
    private String bairro;
}
