package com.devsuperior.userdept.entities.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.userdept.entities.Consultas;
import com.devsuperior.userdept.repositories.ConsultasRepository;

@RestController
@RequestMapping(value = "/consultas")
public class Controller {

	@Autowired//mecanismo de injeção de dependencia...garante usar as própriedades de UserRepository sem precisar instan-
    //ciar 
	private ConsultasRepository repository; //atributo do tipo UserRepository
	
	@GetMapping  //endpoint de consulta
	public List<Consultas> findAll() { /* vai no bd e busca todas as consultas */
        List<Consultas> result = repository.findAll();
		return result;
	}

	@GetMapping(value = "/{id}") //pega consulta por id
	public Consultas findById(@PathVariable Long id) {
		Consultas result = repository.findById(id).get();
		return result;
	}
	
	@PostMapping //endpoint...add consulta
	public Consultas insert(@RequestBody Consultas user) {
		Consultas result = repository.save(user);
		return result;
	}


    @DeleteMapping(value = "/{id}") //endpoint p/ delete
     public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }


    
}