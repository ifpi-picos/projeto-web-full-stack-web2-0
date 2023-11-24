package com.JWTpostegree.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.JWTpostegree.demo.model.Users;

import com.JWTpostegree.demo.repository.UserRepository;
import com.JWTpostegree.demo.utils.JwtTokenUtil;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import java.util.Optional;

    @Controller
    @RequestMapping
    @OpenAPIDefinition
public class ControllerApi {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
   

//-----------------------------enpoint para registrar usuário(s)-----------------------------------
    @PostMapping("/register") 
    @Operation(
    summary = "Novo Usuario",
    description = "Cria um novo usuário no sistema e retorna uma mensagem indicando o sucesso ou falha da operação.",
    method = "POST"
      )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de consultas."),
            @ApiResponse(responseCode = "400", description = "Consultas não encontradas.")
      }
    )
    public ResponseEntity<Object> registerUser(@RequestBody Users user){
        String hasPassword = bCryptPasswordEncoder.encode(user.getSenha());
        user.setSenha(hasPassword);
        ;
        if (userRepository.save(user).getId()>0){
            return ResponseEntity.ok("Usuário criado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuário não salvo.");
    }

   

//--------------------------------------deleta usuário-------------------------------------------------------
@DeleteMapping("/deleteUser/{userId}")
 @Operation(
    summary = "Deletar Usuário",
    description = "Deleta um usuário baseado no id",
    method = "DELETE"
      )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado"),
            @ApiResponse(responseCode = "400", description = "erro ao deletar usário.")
        }
        
)
public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
    try {
        Optional<Users> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            userRepository.deleteById(userId);
            return new ResponseEntity<>("Usuário excluído com sucesso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
    } catch (Exception e) {
        return new ResponseEntity<>("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}


//--------------------------------------Atenticação-----------------------------------------------

@PostMapping("/login")
@Operation(
    summary = "Login de usuário",
    description = "Efetua o login e retorna o token JWT",
    method = "POST"
)
@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    }
)
public ResponseEntity<Object> loginUser(@RequestBody Users loginUser) {
    try {
        Users user = userRepository.findByUsername(loginUser.getUsername());
        if (user != null && bCryptPasswordEncoder.matches(loginUser.getSenha(), user.getSenha())) {
            String token = jwtTokenUtil.generateToken(user.getUsername()); // Correção aqui
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
    }
}

//--------------------------------------------------------------------------------------------





   











}