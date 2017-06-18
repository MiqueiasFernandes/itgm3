package com.itgm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Compartilhar.
 */
@Entity
@Table(name = "compartilhar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "compartilhar")
public class Compartilhar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    private User remetente;

    @ManyToOne
    private User destinatario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public Compartilhar tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Compartilhar mensagem(String mensagem) {
        this.mensagem = mensagem;
        return this;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCodigo() {
        return codigo;
    }

    public Compartilhar codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public Compartilhar nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getStatus() {
        return status;
    }

    public Compartilhar status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public User getRemetente() {
        return remetente;
    }

    public Compartilhar remetente(User user) {
        this.remetente = user;
        return this;
    }

    public void setRemetente(User user) {
        this.remetente = user;
    }

    public User getDestinatario() {
        return destinatario;
    }

    public Compartilhar destinatario(User user) {
        this.destinatario = user;
        return this;
    }

    public void setDestinatario(User user) {
        this.destinatario = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Compartilhar compartilhar = (Compartilhar) o;
        if (compartilhar.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, compartilhar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Compartilhar{" +
            "id=" + id +
            ", tipo='" + tipo + "'" +
            ", mensagem='" + mensagem + "'" +
            ", codigo='" + codigo + "'" +
            ", nome='" + nome + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
