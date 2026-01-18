package com.yowyob.fleet.infrastructure.adapters.inbound.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

 

    @ExceptionHandler(org.springframework.web.reactive.function.client.WebClientResponseException.Unauthorized.class)
    public ProblemDetail handleUnauthorized(org.springframework.web.reactive.function.client.WebClientResponseException.Unauthorized ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Identifiants TraMaSys invalides");
        problem.setTitle("Échec d'authentification");
        return problem;
    }

    @ExceptionHandler(org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest.class)
    public ProblemDetail handleBadRequest(org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest ex) {
        String remoteMessage = ex.getResponseBodyAsString();
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, remoteMessage);
        problem.setTitle("Erreur de validation distante");
        return problem;
    }
}