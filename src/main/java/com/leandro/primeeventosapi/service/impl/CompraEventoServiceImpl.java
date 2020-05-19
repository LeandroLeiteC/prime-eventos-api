package com.leandro.primeeventosapi.service.impl;

import com.leandro.primeeventosapi.domain.entity.*;
import com.leandro.primeeventosapi.domain.enums.StatusCompra;
import com.leandro.primeeventosapi.domain.enums.StatusEvento;
import com.leandro.primeeventosapi.domain.repository.CompraEventoRepository;
import com.leandro.primeeventosapi.domain.repository.CompraRepository;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.service.CompraEventoService;
import com.leandro.primeeventosapi.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CompraEventoServiceImpl implements CompraEventoService {

    private final EventoService eventoService;
    private final CompraEventoRepository repository;
    private final CompraRepository compraRepository;

    @Override
    @Transactional(readOnly = false)
    public List<CompraEvento> saveAll(List<CompraEvento> compraEventos, Compra compra) {

        compraEventos.forEach(compraEvento -> {
            Evento evento = eventoService.findByIdAndStatus(compraEvento.getEvento().getId(), StatusEvento.ABERTO)
                    .orElseThrow(() -> new BussinesException("Evento não encontrado."));

            if(compraEvento.getQtdIngresso() <=0){
                throw new BussinesException("Quantidade de ingressos deve ser maior que 0 (zero).");
            }

            compraEvento.setId(null);
            AtomicReference<Integer> comprados = new AtomicReference<>(0);

            List<Compra> compras = compraRepository.findAllByStatusAndClienteUsuarioEmail(StatusCompra.REALIZADO,compra.getCliente().getUsuario().getEmail());

            if(compras.size() > 0){
                compras.forEach(c -> {
                    c.getCompraEventos().forEach(ce -> {
                        if(evento.getId() == ce.getEvento().getId()){
                            comprados.updateAndGet(v -> v + ce.getQtdIngresso());
                        }
                    });
                });
            }

            if(evento.getIngressosDisponiveis() == 0) {
                throw new BussinesException("Não foi possível efetuar a compra pois os ingressos do evento " + evento.getNome() + " estão esgotados.");
            }

            Integer ingressosCliente = comprados.get() + compraEvento.getQtdIngresso();

            if(ingressosCliente > evento.getLimiteCliente()){
                throw new BussinesException("Compra não efetuada limite por cliente ultrapassado. Você já comprou " + comprados.get() + " do limite de " + evento.getLimiteCliente() + " ingressos do evento " + evento.getNome() + ".");
            }

            Integer novaQtd = evento.getIngressosDisponiveis() - compraEvento.getQtdIngresso();

            if(novaQtd < 0){
                throw new BussinesException("Compra não efetuada somente " + evento.getIngressosDisponiveis() + " ingressos disponíveis.");
            }
            evento.setIngressosDisponiveis(novaQtd);
            evento.setIngressosVendidos(evento.getIngressosVendidos() + compraEvento.getQtdIngresso());

            compraEvento.setEvento(evento);
            compraEvento.setValorUnitario(evento.getPreco());
            compraEvento.setSubtotal(compraEvento.calcularSubtotal());
            compraEvento.setCompra(compra);

        });

        return repository.saveAll(compraEventos);
    }

}
