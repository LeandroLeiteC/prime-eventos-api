package com.leandro.primeeventosapi.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name = "casa_de_show")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CasaDeShow extends EntidadeBase {

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "uf", nullable = false)
    private String uf;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "rua", nullable = false)
    private String rua;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "limite_de_pessoas", nullable = false)
    private Integer limitePessoas;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @OneToMany(mappedBy = "casaDeShow", cascade = CascadeType.ALL)
    private List<Evento> eventos;

    public CasaDeShow() {
        super();
    }

    public CasaDeShow(Long id) {
        super.setId(id);
    }
}
