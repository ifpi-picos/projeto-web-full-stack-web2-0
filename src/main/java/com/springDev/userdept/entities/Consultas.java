package com.devsuperior.userdept.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity /* anotação da JPA que inidca que essa classe será mapeada p/ uma tabela do databese*/
@Table(name = "tb_consultas")
public class Consultas {

    @Id /* siguinifica que o id será a chave primária do banco */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medico;
    private String paciente;
    private String data;
    private String hora;
    private double preco;


    @ManyToOne /* define relacionamento muitos para 1 entre as duas tabelas */
    @JoinColumn(name = "department_id") /* nome da chave estrangeira */
    private Departament departament;


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


    public Departament getDepartament() {
        return departament;
    }


    public void setDepartament(Departament departament) {
        this.departament = departament;
    }

    


    
}
