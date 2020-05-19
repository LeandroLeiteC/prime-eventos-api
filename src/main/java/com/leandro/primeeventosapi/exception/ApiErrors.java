package com.leandro.primeeventosapi.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ApiErrors {

    private List<String> errors;

    public ApiErrors(String errorMessage){
        this.errors = Arrays.asList(errorMessage);
    }

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }
}
