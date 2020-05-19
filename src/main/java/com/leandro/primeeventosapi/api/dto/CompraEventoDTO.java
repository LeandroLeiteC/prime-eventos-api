package com.leandro.primeeventosapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraEventoDTO {

    private EventoDTO evento;
    private Integer qtdIngresso;
    private Double ValorUnitario;
    private Double subtotal;
}
