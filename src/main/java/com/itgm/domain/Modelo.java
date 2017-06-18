package com.itgm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Modelo.
 */
@Entity
@Table(name = "modelo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "modelo")
public class Modelo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cor")
    private String cor;

    @Column(name = "formula")
    private String formula;

    @Column(name = "funcao")
    private String funcao;

    @Column(name = "variaveis")
    private String variaveis;

    @Column(name = "palpite")
    private String palpite;

    @Column(name = "parametros")
    private String parametros;

    @Column(name = "requires")
    private String requires;

    @Column(name = "codigo")
    private String codigo;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Modelo nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public Modelo cor(String cor) {
        this.cor = cor;
        return this;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getFormula() {
        return formula;
    }

    public Modelo formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFuncao() {
        return funcao;
    }

    public Modelo funcao(String funcao) {
        this.funcao = funcao;
        return this;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getVariaveis() {
        return variaveis;
    }

    public Modelo variaveis(String variaveis) {
        this.variaveis = variaveis;
        return this;
    }

    public void setVariaveis(String variaveis) {
        this.variaveis = variaveis;
    }

    public String getPalpite() {
        return palpite;
    }

    public Modelo palpite(String palpite) {
        this.palpite = palpite;
        return this;
    }

    public void setPalpite(String palpite) {
        this.palpite = palpite;
    }

    public String getParametros() {
        return parametros;
    }

    public Modelo parametros(String parametros) {
        this.parametros = parametros;
        return this;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getRequires() {
        return requires;
    }

    public Modelo requires(String requires) {
        this.requires = requires;
        return this;
    }

    public void setRequires(String requires) {
        this.requires = requires;
    }

    public String getCodigo() {
        return codigo;
    }

    public Modelo codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public User getUser() {
        return user;
    }

    public Modelo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Modelo modelo = (Modelo) o;
        if (modelo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, modelo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Modelo{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", cor='" + cor + "'" +
            ", formula='" + formula + "'" +
            ", funcao='" + funcao + "'" +
            ", variaveis='" + variaveis + "'" +
            ", palpite='" + palpite + "'" +
            ", parametros='" + parametros + "'" +
            ", requires='" + requires + "'" +
            ", codigo='" + codigo + "'" +
            '}';
    }
}
