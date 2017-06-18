package com.itgm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "card")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "url")
    private String url;

    @Column(name = "https")
    private Boolean https;

    @Column(name = "meta")
    private String meta;

    @Column(name = "previa")
    private String previa;

    @Column(name = "disposicao")
    private String disposicao;

    @Column(name = "tipo")
    private Integer tipo;

    @Column(name = "linha")
    private Integer linha;

    @Column(name = "coluna")
    private Integer coluna;

    @Column(name = "modo")
    private String modo;

    @Column(name = "caminho")
    private String caminho;

    @Column(name = "arquivo")
    private String arquivo;

    @Column(name = "extensao")
    private String extensao;

    @Column(name = "largura")
    private Integer largura;

    @Column(name = "classe")
    private String classe;

    @Column(name = "codigo")
    private String codigo;

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

    public Card nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public Card url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isHttps() {
        return https;
    }

    public Card https(Boolean https) {
        this.https = https;
        return this;
    }

    public void setHttps(Boolean https) {
        this.https = https;
    }

    public String getMeta() {
        return meta;
    }

    public Card meta(String meta) {
        this.meta = meta;
        return this;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getPrevia() {
        return previa;
    }

    public Card previa(String previa) {
        this.previa = previa;
        return this;
    }

    public void setPrevia(String previa) {
        this.previa = previa;
    }

    public String getDisposicao() {
        return disposicao;
    }

    public Card disposicao(String disposicao) {
        this.disposicao = disposicao;
        return this;
    }

    public void setDisposicao(String disposicao) {
        this.disposicao = disposicao;
    }

    public Integer getTipo() {
        return tipo;
    }

    public Card tipo(Integer tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getLinha() {
        return linha;
    }

    public Card linha(Integer linha) {
        this.linha = linha;
        return this;
    }

    public void setLinha(Integer linha) {
        this.linha = linha;
    }

    public Integer getColuna() {
        return coluna;
    }

    public Card coluna(Integer coluna) {
        this.coluna = coluna;
        return this;
    }

    public void setColuna(Integer coluna) {
        this.coluna = coluna;
    }

    public String getModo() {
        return modo;
    }

    public Card modo(String modo) {
        this.modo = modo;
        return this;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getCaminho() {
        return caminho;
    }

    public Card caminho(String caminho) {
        this.caminho = caminho;
        return this;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getArquivo() {
        return arquivo;
    }

    public Card arquivo(String arquivo) {
        this.arquivo = arquivo;
        return this;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getExtensao() {
        return extensao;
    }

    public Card extensao(String extensao) {
        this.extensao = extensao;
        return this;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public Integer getLargura() {
        return largura;
    }

    public Card largura(Integer largura) {
        this.largura = largura;
        return this;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }

    public String getClasse() {
        return classe;
    }

    public Card classe(String classe) {
        this.classe = classe;
        return this;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getCodigo() {
        return codigo;
    }

    public Card codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cenario getCenario() {
        return cenario;
    }

    public Card cenario(Cenario cenario) {
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
        Card card = (Card) o;
        if (card.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Card{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", url='" + url + "'" +
            ", https='" + https + "'" +
            ", meta='" + meta + "'" +
            ", previa='" + previa + "'" +
            ", disposicao='" + disposicao + "'" +
            ", tipo='" + tipo + "'" +
            ", linha='" + linha + "'" +
            ", coluna='" + coluna + "'" +
            ", modo='" + modo + "'" +
            ", caminho='" + caminho + "'" +
            ", arquivo='" + arquivo + "'" +
            ", extensao='" + extensao + "'" +
            ", largura='" + largura + "'" +
            ", classe='" + classe + "'" +
            ", codigo='" + codigo + "'" +
            '}';
    }
}
