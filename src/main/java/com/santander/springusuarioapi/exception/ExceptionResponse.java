package com.santander.springusuarioapi.exception;

import com.santander.springusuarioapi.exception.negocio.NegocioException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

public class ExceptionResponse {

    public int status;
    public String timestamp;
    public String titulo;
    public String descricao;
    public String path;
    public Map<String, String> errors;

    public ExceptionResponse(NotFoundException e, WebRequest request) {
        this.status = HttpStatus.NOT_FOUND.value();
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Recurso não encontrado";
        this.descricao = e.getMessage();
        this.path = request.getDescription(false);
    }

    public ExceptionResponse(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        this.status = HttpStatus.BAD_REQUEST.value();
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Chamada inválida";
        this.descricao = "Error nos dados fornecidos para o recurso";
        this.path = request.getDescription(false);
        this.errors = errors;
    }

    public ExceptionResponse(NegocioException e, WebRequest request) {
        this.status = HttpStatus.UNPROCESSABLE_ENTITY.value();
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Negócio";
        this.descricao = e.getMessage();
        this.path = request.getDescription(false);
    }

    public ExceptionResponse(InternalServerErrorException e, WebRequest request) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.timestamp = Evento.getFormattedTimestamp();
        this.titulo = "Error de aplicação";
        this.descricao = e.getMessage();
        this.path = request.getDescription(false);
    }

}
