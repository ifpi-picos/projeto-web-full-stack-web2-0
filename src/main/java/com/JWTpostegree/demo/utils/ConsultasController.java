
package com.JWTpostegree.demo.utils;
import java.util.List;
import java.util.Optional;
//import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.hibernate.mapping.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.JWTpostegree.demo.Consultas;
import com.JWTpostegree.demo.controller.PreAuthorize;
import com.JWTpostegree.demo.repository.ConsultasRepository;



import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Controller
@RequestMapping
@OpenAPIDefinition
public class ConsultasController {

    @Autowired
    private ConsultasRepository consultasRepository;

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


    
}
