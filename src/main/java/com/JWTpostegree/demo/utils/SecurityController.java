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
import java.util.Optional;  // Adicione ou mantenha essa importação, dependendo da sua escolha

@Controller
@RequestMapping
@OpenAPIDefinition
public class SecurityController {


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; //Criptografia de senhas
   

//-----------------------------enpoint para gerar token--------------------------------------

@PostMapping("/generate-token") 
@Operation( //openapi documentation / descrição da requisiçaõ
summary = "Generate Token",
description = "gera um token para usuarios cadastrados.",
method = "GET"
  )
@ApiResponses(//openapi documentation / estatus resposta
    value = {
        @ApiResponse(responseCode = "200", description = "Retorna um token"),
        @ApiResponse(responseCode = "400", description = "erro ao gerar gerar token")
 }
)
public ResponseEntity<Object> generateToken(@RequestBody TokenReqRes tokenReqRes){
    Users databaseUser = userRepository.findByUsername(tokenReqRes.getUsername());
    if (databaseUser == null){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Desculpe, o usuário não existe");
    }      //Criptografia de senhas
    if (new BCryptPasswordEncoder().matches(tokenReqRes.getSenha(), databaseUser.getSenha())){
        String token = jwtTokenUtil.generateToken(tokenReqRes.getUsername());
        tokenReqRes.setToken(token);
        tokenReqRes.setExpirationTime("6000 Sec"); //TokenReqRes --> em TokenReqRes
        return ResponseEntity.ok(tokenReqRes);
        //O token é então retornado na resposta da solicitação de autenticação.
    }else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("A senha não corresponde");
    }
}







@PostMapping("/validate-token") //validar token
public ResponseEntity<Object> validateToken(@RequestBody TokenReqRes tokenReqRes){
    return ResponseEntity.ok(jwtTokenUtil.validateToken(tokenReqRes.getToken()));
}
//=================================================================================================

//-----------------------------enpoint para registrar usuário(s)-----------------------------------
@PostMapping("/register")
@Operation(
        summary = "Registro de Usuário",
        description = "Cria um novo usuário no sistema e retorna um token JWT.",
        method = "POST"
)
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso."),
                @ApiResponse(responseCode = "400", description = "Erro ao registrar usuário.")
        }
)
public ResponseEntity<Object> registerUser(@RequestBody Users user) {
    try {
        //o hash da senha antes de salvar
        String hashedPassword = bCryptPasswordEncoder.encode(user.getSenha());
        user.setSenha(hashedPassword);

        //Cria um novo usuário e associe as consultas
        Users savedUser = new Users();
        savedUser.setUsername(user.getUsername());
        savedUser.setSenha(user.getSenha());

        //Salva o usuário
        Users registeredUser = userRepository.save(savedUser);

        //Cria um token para o novo usuário
        String token = jwtTokenUtil.generateToken(registeredUser.getUsername());

        //Retorne o token e outras informações, se necessário
        TokenReqRes response = new TokenReqRes();
        response.setToken(token);
        response.setExpirationTime("6000 Sec"); 
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar usuário.");
    }
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
public ResponseEntity<TokenReqRes> loginUser(@RequestBody Users loginUser) {
    try {
        Users user = userRepository.findByUsername(loginUser.getUsername());
        if (user != null && bCryptPasswordEncoder.matches(loginUser.getSenha(), user.getSenha())) {
            String token = jwtTokenUtil.generateToken(user.getUsername());

            TokenReqRes response = new TokenReqRes();
            response.setToken(token);
            response.setExpirationTime("6000 Sec"); // Substitua pelo tempo real de expiração do token

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}






    
}
