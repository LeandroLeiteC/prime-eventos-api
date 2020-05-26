package com.leandro.primeeventosapi.service.impl;

import com.leandro.primeeventosapi.domain.entity.CasaDeShow;
import com.leandro.primeeventosapi.domain.entity.Evento;
import com.leandro.primeeventosapi.domain.enums.StatusEvento;
import com.leandro.primeeventosapi.domain.repository.EventoRepository;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.exception.ObjetoNotFoundException;
import com.leandro.primeeventosapi.service.CasaDeShowService;
import com.leandro.primeeventosapi.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventoServiceImpl implements EventoService {

    private final EventoRepository repository;
    private final CasaDeShowService casaService;

    @Value("${evento.foto}")
    private String PATH_FOTO;

    @Override
    @Transactional(readOnly = false)
    public Evento save(Evento evento) {
        CasaDeShow casaDeShow = casaService.findById(evento.getCasaDeShow().getId()).orElseThrow(() -> new BussinesException("Casa de show não encontrado."));
        if(evento.getIngressosDisponiveis() > casaDeShow.getLimitePessoas()){
            throw new BussinesException("Ingressos do evento não pode ser maior que o limite permitido pela casa de show.");
        }

        if(evento.getIngressosDisponiveis() < evento.getLimiteCliente()){
            throw new BussinesException("O limite de ingressos por cliente deve ser menor ou igual ao número de ingressos disponíveis.");
        }
        evento.setId(null);
        evento.setIngressosVendidos(0);
        evento.setStatus(StatusEvento.ABERTO);
        evento.setCasaDeShow(casaDeShow);
        return repository.save(evento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evento> findAll(Example example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveImage(MultipartFile foto, Long id, String tipo) {
        Evento evento = repository.findById(id).orElseThrow(() -> new ObjetoNotFoundException("Evento não encontrado."));
        tipo = tipo.toLowerCase();
        try{
            if(tipo.equals("card") && evento.getNomeImagemCard() != null){
                Path path = Paths.get(PATH_FOTO, evento.getNomeImagemCard());
                Files.delete(path);
            }
            if(tipo.equals("banner") && evento.getNomeImagemBanner() != null){
                Path path = Paths.get(PATH_FOTO, evento.getNomeImagemBanner());
                Files.delete(path);
            }

            String arquivo = nomeDoArquivo(foto.getName(), foto.getContentType());
            byte[] bytes = foto.getBytes();
            Path path = Paths.get(PATH_FOTO, arquivo);
            Files.write(path, bytes);

            if(tipo.equals("card")){
                evento.setNomeImagemCard(arquivo);
            }else if(tipo.equals("banner")){
                evento.setNomeImagemBanner(arquivo);
            }else{
                throw new BussinesException("Tipo da imagem inválido ('card' ou 'banner').");
            }
        }catch (IOException e){
            e.printStackTrace();
            throw  new BussinesException("Não foi possível salvar a foto");
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void updateStatus(Evento evento, StatusEvento status) {
        evento.setStatus(status);
    }

    @Override
    public Optional<Evento> findByIdAndStatus(Long id, StatusEvento status) {
        return repository.findByIdAndStatus(id, status);
    }

    @Override
    public Optional<Evento> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Evento update(Evento evento) {
        return repository.save(evento);
    }

    private String nomeDoArquivo(String nome, String tipo) {
        String novoNome = Timestamp.valueOf(LocalDateTime.now()).toString().concat(nome);
        String nomeBas64 = Base64.getEncoder().encodeToString(novoNome.getBytes());
        String pegarTipo = tipo.split("/")[1];
        return nomeBas64 + "." + pegarTipo;
    }

}
