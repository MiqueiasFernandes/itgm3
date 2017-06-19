package com.itgm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Cenario.
 */
@Entity
@Table(name = "cenario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cenario")
public class Cenario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "caminho")
    private String caminho;

    @ManyToOne
    private Projeto projeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Cenario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminho() {
        return caminho;
    }

    public Cenario caminho(String caminho) {
        this.caminho = caminho;
        return this;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public Cenario projeto(Projeto projeto) {
        this.projeto = projeto;
        return this;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cenario cenario = (Cenario) o;
        if (cenario.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cenario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cenario{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", caminho='" + caminho + "'" +
            '}';
    }
}
