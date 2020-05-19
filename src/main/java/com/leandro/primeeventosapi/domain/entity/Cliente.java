package com.leandro.primeeventosapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "cliente")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Cliente extends EntidadeBase {

    @Column(name = "nome", unique = false, nullable = false)
    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Compra> compras;

    public Cliente() {
        super();
    }

    public Cliente(Long id) {
        super.setId(id);
    }
}
