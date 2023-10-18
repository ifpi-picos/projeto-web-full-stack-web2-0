package com.JWTpostegree.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.JWTpostegree.demo.Consultas;
import com.JWTpostegree.demo.model.TokenReqRes;
import com.JWTpostegree.demo.model.Users;
import com.JWTpostegree.demo.repository.ConsultasRepository;
import com.JWTpostegree.demo.repository.UserRepository;
import com.JWTpostegree.demo.utils.JwtTokenUtil;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
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
    @Autowired
    private ConsultasRepository consultasRepository;


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
        String hashedPassword = bCryptPasswordEncoder.encode(user.getSenha());
        user.setSenha(hashedPassword);
        ;
        if (userRepository.save(user).getId()>0){
            return ResponseEntity.ok("Usuário criado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuário não salvo.");
    }
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

   

//--------------------------consultas, apenas usuários autenticados podem acessar:-------------------------------

    @GetMapping("/consultas")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
    summary = "Consultas",
    description = "Exibe todas as consultas salvas no banco de dados.",
    method = "GET"
      )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "consultas listadas com sucesso."),
            @ApiResponse(responseCode = "400", description = "erro ao buscar consultas.")
        }
    )
    public ResponseEntity<List<Consultas>> getAllConsultas() {
        return ResponseEntity.ok(consultasRepository.findAll());
    }


//-------------------------consultas/{id}, apenas usuários autenticados podem acessar:-----------------

    @GetMapping("/consultas/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
     @Operation(
    summary = "Consultas Por id",
    description = "busca uma consulta por id",
    method = "GET"
      )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "consulta listada com sucesso."),
            @ApiResponse(responseCode = "400", description = "erro ao buscar consulta.")
        }
    )
    public ResponseEntity<Consultas> getConsultaById(@PathVariable Long id) {
        Optional<Consultas> consulta = consultasRepository.findById(id);
        if(consulta.isPresent()){
            return ResponseEntity.ok(consulta.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


//-----------------------------Cria uma nova consulta-----------------------------------------------------
   

    @PostMapping("/newConsulta")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
    summary = "Nova Consulta",
    description = "Cria uma nova consulta, com: medico, paciente, data , hora , preco e departamento",
    method = "GET"
      )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "consulta criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "erro ao criar consulta.")
        }
    )
    public ResponseEntity<Consultas> createConsulta(@RequestBody Consultas consulta) {
        Consultas newConsulta = consultasRepository.save(consulta);
        return new ResponseEntity<>(newConsulta, HttpStatus.CREATED);
    }

    
//------------------------------Deleta uma consulta----------------------------------------------------
    

    @DeleteMapping("/deleteConsulta/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
     @Operation(
    summary = "Deleta Consulta",
    description = "Deleta uma consulta pelo id",
    method = "GET"
      )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Consulta excluida."),
            @ApiResponse(responseCode = "400", description = "erro ao excluir consulta.")
        }
    )
    public ResponseEntity<String> deleteConsulta(@PathVariable Long id) {
        consultasRepository.deleteById(id);
        return new ResponseEntity<>("Consulta deletada com sucesso!", HttpStatus.OK);
    }


//------------------------------Edita uma consulta-----------------------------------------------------
    
    @PutMapping("/consultas/{id}")
   @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(
    summary = "Editar Consulta",
    description = "Edita uma consulta existente.",
    method = "GET"
      )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "consulta editada."),
            @ApiResponse(responseCode = "400", description = "erro ao editar consulta.")
        }
    )
  public ResponseEntity<Consultas> updateConsulta(@PathVariable Long id, @RequestBody Consultas consultaDetails) {
    Optional<Consultas> consultaOptional = consultasRepository.findById(id);
    
    if (consultaOptional.isPresent()) {
        Consultas consulta = consultaOptional.get();
        
        //Atualiza os detalhes da consulta aqui
        consulta.setMedico(consultaDetails.getMedico());
        consulta.setPaciente(consultaDetails.getPaciente());
        consulta.setData(consultaDetails.getData());
        consulta.setHora(consultaDetails.getHora());
        consulta.setPreco(consultaDetails.getPreco());
        //consulta.setDepartment(consultaDetails.getDepartment());
        
        // Salve a consulta atualizada
        consultasRepository.save(consulta);
        return ResponseEntity.ok(consulta);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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


//-------------------------------------------------------------------------------------------------



   











}