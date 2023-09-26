package com.devsuperior.userdept.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.userdept.entities.Consultas;



/* instancia jpa , ascessa banco de dados... */


public interface ConsultasRepository extends JpaRepository<Consultas, Long> {
   
    

}