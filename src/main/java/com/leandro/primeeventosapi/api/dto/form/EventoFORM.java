package com.leandro.primeeventosapi.api.dto.form;

import com.leandro.primeeventosapi.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoFORM {

    @NotEmpty(message = "Campo nome é obrigatório.")
    @Size(max = 40, message = "Nome do evento deve ter nom máximo 40 caracteres.")
    private String nome;

    @NotEmpty(message = "Campo nome é obrigatório")
    private String descricao;

    @NotEmpty(message = "Campo pequena descrição é obrigatório")
    @Size(max = 120, message = "Campo pequena descrição deve ter no máximo 120 caracteres.")
    private String pequenaDescricao;

    @NotNull(message = "Campo id casa de show é obrigatório.")
    @Positive(message = "Campo Id casa de show deve ser maior que 0 (zero).")
    private Long idCasaDeShow;

    @NotNull(message = "Campo data do evento é obrigatório.")
    @FutureOrPresent(message = "A data do evento deve superior ou igual a data atual.")
    private LocalDateTime data;

    @NotNull(message = "O campo preço do ingresso é obrigatório.")
    @PositiveOrZero(message = "O valor do preço do ingresso deve ser maior ou igual a 0 (zero).")
    private Double preco;

    @NotNull(message = "Campo ingressos disponíveis é obrigatório.")
    @Positive(message = "O número de ingresso disponíveis deve ser maior que 0 (zero).")
    @Min(value = 10, message = "O mínimo são 10 ingressos.")
    private int ingressosDisponiveis;

    @NotNull(message = "Campo limite de ingressos por cliente é obrigatório.")
    @Positive(message = "O número de ingressos por cliente deve ser maior que 0 (zero).")
    private int limiteCliente;

    @NotNull(message = "Campo status é obrigatório.")
    private Status status;
}
