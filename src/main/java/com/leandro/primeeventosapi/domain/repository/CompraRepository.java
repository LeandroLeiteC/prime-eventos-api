package com.leandro.primeeventosapi.domain.repository;

import com.leandro.primeeventosapi.domain.entity.Compra;
import com.leandro.primeeventosapi.domain.enums.StatusCompra;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {

//    @Query("select c from Compra c where c.status = :status and c.usuario.email = :email")
    List<Compra> findAllByStatusAndUsuarioEmail(StatusCompra status, String email);

    List<Compra> findAllByUsuarioEmail(String email);

    Optional<Compra> findByIdAndUsuarioEmail(Long id, String email);

}
