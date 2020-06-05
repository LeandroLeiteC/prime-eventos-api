package com.leandro.primeeventosapi.domain.entity;

import com.leandro.primeeventosapi.domain.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Table(name = "evento")
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Evento extends EntidadeBase {

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "pequena_descricao", nullable = false, length = 120)
    private String pequenaDescricao;

    @ManyToOne
    @JoinColumn(name = "casa_de_show")
    private CasaDeShow casaDeShow;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Column(name = "preco", nullable = false)
    private Double preco;

    @Column(name = "ingressos_disponiveis", nullable = false)
    private Integer ingressosDisponiveis;

    @Column(name ="ingressos_vendidos", nullable = false)
    private Integer ingressosVendidos;

    @Column(name = "limite_ingressos_por_cliente", nullable = false)
    private Integer limiteCliente;

    @Column(name = "nome_imagem_card")
    private String nomeImagemCard;

    @Column(name = "nome_imagem_banner")
    private String nomeImagemBanner;

    @Column(name = "status")
    private Status status;

    public Evento() {
        super();
    }

    public Evento(Long id) {
        super.setId(id);
    }

}