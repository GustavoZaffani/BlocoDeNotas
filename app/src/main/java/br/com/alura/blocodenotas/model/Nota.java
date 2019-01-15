package br.com.alura.blocodenotas.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Nota implements Serializable {

    private Integer id;
    private String titulo;
    private String descricao;
    private Date data;

    public Nota(String titulo, String descricao, Date data) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
    }

    public Nota() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
