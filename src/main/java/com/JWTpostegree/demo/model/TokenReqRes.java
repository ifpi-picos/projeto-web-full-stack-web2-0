package com.JWTpostegree.demo.model;

//solicitação resposta api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Uma anotação do projeto Lombok que gera automaticamente métodos como toString, equals, hashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenReqRes {

    private String username;
    private String senha;

    private String token;
    private String expirationTime;
}

/*
a classe TokenReqRes é uma representação dos dados utilizados nas solicitações e respostas relacionadas à geração de tokens.
 * Ao fazer uma solicitação para gerar um token (como no endpoint /generate-token), será enviado um objeto TokenReqRes contendo username e senha. O servidor, por meio do método generateToken do SecurityController, validaria essas credenciais e, se válidas, geraria um token JWT.
A resposta do servidor para essa solicitação seria um objeto TokenReqRes contendo o token gerado e possivelmente outras informações, dependendo da implementação.
 */






