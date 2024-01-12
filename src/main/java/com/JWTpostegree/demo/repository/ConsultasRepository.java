package com.JWTpostegree.demo.repository;

import com.JWTpostegree.demo.Consultas;
import com.JWTpostegree.demo.model.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultasRepository extends JpaRepository<Consultas, Long> {
  List<Consultas> findByUser(Users user);
    // Aqui pode ser adicionado métodos de pesquisa personalizados...
}

/*
  ConsultasRepository é uma interface que estende JpaRepository. Assim como no UserRepository, ela herda métodos prontos para operações no banco de dados relacionadas a classe Consultas.
 */


