package com.JWTpostegree.demo;

import com.JWTpostegree.demo.model.Users;

import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity /* anotação da JPA que inidca que essa classe será mapeada p/ uma tabela do databese*/
@Table(name = "tb_consultas")
public class Consultas {

    @Id /* siguinifica que o id será a chave primária do banco */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    private String medico;
    private String paciente;
    private String data;
    private String hora;
    private double preco;
    private String departamento;

    

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getMedico() {
        return medico;
    }


    public void setMedico(String medico) {
        this.medico = medico;
    }


    public String getPaciente() {
        return paciente;
    }


    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }


    public String getData() {
        return data;
    }


    public void setData(String data) {
        this.data = data;
    }


    public String getHora() {
        return hora;
    }


    public void setHora(String hora) {
        this.hora = hora;
    }


    public double getPreco() {
        return preco;
    }


    public void setPreco(double preco) {
        this.preco = preco;
    }


   
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    


    
}
