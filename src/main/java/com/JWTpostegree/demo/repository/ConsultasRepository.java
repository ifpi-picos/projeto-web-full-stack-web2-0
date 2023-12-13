package com.JWTpostegree.demo.repository;

import com.JWTpostegree.demo.Consultas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultasRepository extends JpaRepository<Consultas, Long> {
    // Aqui pode ser adicionado métodos de pesquisa personalizados...
}

/*
  ConsultasRepository é uma interface que estende JpaRepository. Assim como no UserRepository, ela herda métodos prontos para operações no banco de dados relacionadas a classe Consultas.
 */


