package com.JWTpostegree.demo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Data
@Entity /* anotação da JPA que inidca que essa classe será mapeada p/ uma tabela do databese*/
@Table(name = "tb_departamentos")
public class Departamentos {

    @Id /* siguinifica que o id será a chave primária do banco */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    public Departamentos() {
       /*construotor vazio significa que a classe pode ser instanciada sem passar nem um argumento*/
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    
    
    
    
}
