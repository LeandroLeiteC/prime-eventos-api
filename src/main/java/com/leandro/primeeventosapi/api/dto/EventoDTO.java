package com.leandro.primeeventosapi.api.dto;

import com.leandro.primeeventosapi.domain.enums.StatusEvento;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String pequenaDescricao;
    private CasaDeShowDTO casaDeShow;
    private LocalDateTime data;
    private Double preco;
    private String nomeImagemCard;
    private String nomeImagemBanner;
    private StatusEvento status;
    private int ingressosDisponiveis;
    private int ingressosVendidos;
    private int limiteCliente;
}
