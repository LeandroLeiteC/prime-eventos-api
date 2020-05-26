package com.leandro.primeeventosapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "usuario", indexes = { @Index(name = "idx_usuario_email", columnList = "email") })
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Usuario extends EntidadeBase {

    @Column(name = "nome", unique = false, nullable = false)
    private String nome;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "senha", nullable = false)
    private String password;

    @Column(name = "admin", nullable = false)
    private boolean admin;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    public Usuario() {
        super();
    }

    public Usuario(Long id) {
        super.setId(id);
    }

}
