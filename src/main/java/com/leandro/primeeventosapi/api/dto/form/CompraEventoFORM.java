package com.leandro.primeeventosapi.api.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompraEventoFORM {

    @NotNull(message = "Campo id do evento é obrigatório.")
    @Positive(message = "O id do evento deve ser maior que 0 (zero).")
    private Long idEvento;

    @NotNull(message = "Campo quantidade de ingressos é obrigatório.")
    @Positive(message = "Quantidade de ingressos deve ser maior que 0 (zero).")
    private Integer qtdIngresso;
}
