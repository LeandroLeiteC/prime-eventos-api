package com.leandro.primeeventosapi.api.dto.filtro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoFILTRO {

    private String casaDeShow;
    private String cidade;
    private String uf;
    private String bairro;

    private String evento;
}
