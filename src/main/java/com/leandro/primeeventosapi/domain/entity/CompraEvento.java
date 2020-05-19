package com.leandro.primeeventosapi.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "compra_evento")
@Getter
@Setter
@ToString
public class CompraEvento extends EntidadeBase {

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "quantidade_de_ingresso", nullable = false)
    private int qtdIngresso = 1;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    @Column(name = "valor_hora_compra")
    private double valorUnitario;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    public CompraEvento() {
        super();
    }

    public CompraEvento(Long id) {
        super.setId(id);
    }

    public Double calcularSubtotal(){
        return this.valorUnitario * this.qtdIngresso;
    }
}
