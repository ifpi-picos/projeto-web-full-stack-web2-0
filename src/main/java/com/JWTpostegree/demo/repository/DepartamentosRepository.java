package com.JWTpostegree.demo.repository;


import com.JWTpostegree.demo.Departamentos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DepartamentosRepository extends JpaRepository<Departamentos, Long> {
    // Aqui podem ser adicionados métodos de pesquisa personalizados para Departamento, se necessário.
    
}
