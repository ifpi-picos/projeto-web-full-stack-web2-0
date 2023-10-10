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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class ControllerApi {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ConsultasRepository consultasRepository;



    @PostMapping("/register") //enpoint para registrar usuário(s)
    public ResponseEntity<Object> registerUser(@RequestBody Users user){
        String hashedPassword = bCryptPasswordEncoder.encode(user.getSenha());
        user.setSenha(hashedPassword);
        ;
        if (userRepository.save(user).getId()>0){
            return ResponseEntity.ok("O usuário foi salvo");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Usuário não salvo.");
    }



    @PostMapping("/generate-token") //enpoint para gerar token
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




    @PostMapping("/validate-token")
    public ResponseEntity<Object> validateToken(@RequestBody TokenReqRes tokenReqRes){
        return ResponseEntity.ok(jwtTokenUtil.validateToken(tokenReqRes.getToken()));
    }



   //=================================================================================================

   


    // /consultas, apenas usuários autenticados podem acessar:

    @GetMapping("/consultas")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Consultas>> getAllConsultas() {
        return ResponseEntity.ok(consultasRepository.findAll());
    }

    //  /consultas/{id}, apenas usuários autenticados podem acessar:

        @GetMapping("/consultas/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Consultas> getConsultaById(@PathVariable Long id) {
        Optional<Consultas> consulta = consultasRepository.findById(id);
        if(consulta.isPresent()){
            return ResponseEntity.ok(consulta.get());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //  /newConsulta, apenas usuários autenticados podem criar uma nova consulta:

    @PostMapping("/newConsulta")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Consultas> createConsulta(@RequestBody Consultas consulta) {
        Consultas newConsulta = consultasRepository.save(consulta);
        return new ResponseEntity<>(newConsulta, HttpStatus.CREATED);
    }

    

    // /deleteConsulta/{id}, apenas usuários autenticados podem deletar uma consulta:

        @DeleteMapping("/deleteConsulta/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteConsulta(@PathVariable Long id) {
        consultasRepository.deleteById(id);
        return new ResponseEntity<>("Consulta deletada com sucesso!", HttpStatus.OK);
    }


    ///editarConsulta, apenas usuários autenticados podem editar uma consulta:
    
    @PutMapping("/consultas/{id}")
   @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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

    





   











}