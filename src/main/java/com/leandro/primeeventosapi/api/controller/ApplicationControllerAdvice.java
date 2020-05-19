package com.leandro.primeeventosapi.api.controller;

import com.leandro.primeeventosapi.exception.ApiErrors;
import com.leandro.primeeventosapi.exception.BussinesException;
import com.leandro.primeeventosapi.exception.ObjetoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                                    .getAllErrors()
                                    .stream()
                                    .map(erro -> erro.getDefaultMessage())
                                .collect(Collectors.toList());

        return new ApiErrors(errors);
    }

    @ExceptionHandler(BussinesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBussinesException(BussinesException ex){
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleEmptyJsonException(HttpMessageNotReadableException ex){
        return new ApiErrors("Erro no corpo da requisição.");
    }

    @ExceptionHandler(ObjetoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleEmptyJsonException(ObjetoNotFoundException ex){
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMultipartException(MaxUploadSizeExceededException ex){
        return new ApiErrors("Arquivo de imagem enviado excedeu o tamanho permitido (1mb).");
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMultipartException(MultipartException ex){
        return new ApiErrors("Arquivo de imagem não enviado.");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMultipartException(MissingServletRequestPartException ex){
        return new ApiErrors("Arquivo de imagem não enviado.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMultipartException(MissingServletRequestParameterException ex){
        return new ApiErrors("Algum parâmetro da requisição está faltando.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrors handleAcessDenied(AccessDeniedException ex){
        return new ApiErrors("Acesso negado.");
    }

}
