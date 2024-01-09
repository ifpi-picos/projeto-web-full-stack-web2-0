package com.JWTpostegree.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.JWTpostegree.demo.Consultas;
import com.JWTpostegree.demo.Departamentos;
import com.JWTpostegree.demo.controller.PreAuthorize;
import com.JWTpostegree.demo.model.Users;
import com.JWTpostegree.demo.model.TokenReqRes;
//import com.JWTpostegree.demo.model.Users;
import com.JWTpostegree.demo.repository.ConsultasRepository;
import com.JWTpostegree.demo.repository.UserRepository;
import com.JWTpostegree.demo.utils.JwtTokenUtil;
import com.JWTpostegree.demo.repository.DepartamentosRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;




@Controller
@RequestMapping
@OpenAPIDefinition
public class ConsultasController {

    @Autowired
    private ConsultasRepository consultasRepository;

    @Autowired
    private DepartamentosRepository departamentosRepository;

    
    @Autowired
    private UserRepository userRepository;

//--------------------------consultas, apenas usuários autenticados podem acessar:-------------------------------
@GetMapping("/user/{username}")
@Operation(
    summary = "Consultas do Usuário",
    description = "Retorna as consultas associadas a um usuário específico.",
    method = "GET"
)
@ApiResponses(
    value = {
        @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    }
)
public ResponseEntity<List<Consultas>> consultarConsultasPorUsuario(@PathVariable String username) {
    Users user = userRepository.findByUsername(username);
    List<Consultas> consultas = consultasRepository.findByUser(user);
    return ResponseEntity.ok(consultas);
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
    description = "Cria uma nova consulta, com: medico, paciente, data, hora, preco e departamento",
    method = "POST"
)
@ApiResponses(
    value = {
        @ApiResponse(responseCode = "201", description = "consulta criada com sucesso."),
        @ApiResponse(responseCode = "400", description = "erro ao criar consulta.")
    }
)
public ResponseEntity<Consultas> createConsulta(@RequestBody Consultas consulta) {
    // Verifica se o departamento fornecido é válido
    if (consulta.getDepartamento() != null) {
        Consultas newConsulta = consultasRepository.save(consulta);
        return new ResponseEntity<>(newConsulta, HttpStatus.CREATED);
    } else {
        // Lógica para lidar com a falta de departamento
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
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



    
}
