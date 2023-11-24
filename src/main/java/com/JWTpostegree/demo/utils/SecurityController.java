package com.JWTpostegree.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.JWTpostegree.demo.model.TokenReqRes;
import com.JWTpostegree.demo.model.Users;

import com.JWTpostegree.demo.repository.UserRepository;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;






@Controller
@RequestMapping
@OpenAPIDefinition
public class SecurityController {


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
   

//-----------------------------enpoint para gerar token--------------------------------------

@PostMapping("/generate-token") 
@Operation(
summary = "Generate Token",
description = "gera um token para usuarios cadastrados.",
method = "GET"
  )
@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Retorna um token"),
        @ApiResponse(responseCode = "400", description = "erro ao gerar gerar token")
 }
)
public ResponseEntity<Object> generateToken(@RequestBody TokenReqRes tokenReqRes){
    Users databaseUser = userRepository.findByUsername(tokenReqRes.getUsername());
    if (databaseUser == null){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Desculpe, o usuário não existe");
    }
    if (new BCryptPasswordEncoder().matches(tokenReqRes.getSenha(), databaseUser.getSenha())){
        String token = jwtTokenUtil.generateToken(tokenReqRes.getUsername());
        tokenReqRes.setToken(token);
        tokenReqRes.setExpirationTime("6000 Sec"); //TokenReqRes --> em TokenReqRes
        return ResponseEntity.ok(tokenReqRes);
    }else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("A senha não corresponde");
    }
}







@PostMapping("/validate-token") //validar token
public ResponseEntity<Object> validateToken(@RequestBody TokenReqRes tokenReqRes){
    return ResponseEntity.ok(jwtTokenUtil.validateToken(tokenReqRes.getToken()));
}
//=================================================================================================


    
}
