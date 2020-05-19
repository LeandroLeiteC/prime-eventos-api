package com.leandro.primeeventosapi.api.dto;

import com.leandro.primeeventosapi.domain.enums.StatusCompra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {

    private Long id;
    private Double total;
    private LocalDateTime hora;
    private StatusCompra statusCompra;
    private List<CompraEventoDTO> compraEventos;
}
