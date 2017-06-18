package com.itgm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Terminal.
 */
@Entity
@Table(name = "terminal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "terminal")
public class Terminal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    private Cenario cenario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Terminal nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public Terminal url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public Terminal status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Cenario getCenario() {
        return cenario;
    }

    public Terminal cenario(Cenario cenario) {
        this.cenario = cenario;
        return this;
    }

    public void setCenario(Cenario cenario) {
        this.cenario = cenario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Terminal terminal = (Terminal) o;
        if (terminal.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, terminal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Terminal{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", url='" + url + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
