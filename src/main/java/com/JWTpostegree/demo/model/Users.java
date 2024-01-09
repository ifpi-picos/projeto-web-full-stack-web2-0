package com.JWTpostegree.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.JWTpostegree.demo.Consultas;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

//ok

@Data
@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String senha;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Consultas> consultas = new ArrayList<>();
}
