package com.leandro.primeeventosapi.domain.entity;

import com.leandro.primeeventosapi.domain.enums.StatusCompra;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "compra")
@Getter
@Setter
@ToString
public class Compra extends EntidadeBase {

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CompraEvento> compraEventos;

    @Column(name = "hora_da_venda", nullable = false)
    private LocalDateTime hora;

    @Column(name = "total")
    private double total;

    @Column(name = "status")
    private StatusCompra status;

    public Compra() {
        super();
    }

    public Compra(Long id) {
        super.setId(id);
    }

    public double calcularTotal() {
        double t = 0;
        for(CompraEvento ce : compraEventos) {
            t += ce.getSubtotal();
        }
        return t;
    }
}
