package com.itgm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Customize.
 */
@Entity
@Table(name = "customize")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customize")
public class Customize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sidebar")
    private Boolean sidebar;

    @Column(name = "color")
    private String color;

    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    @Column(name = "desktop")
    private String desktop;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private Projeto projeto;

    @OneToOne
    @JoinColumn(unique = true)
    private Cenario cenario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSidebar() {
        return sidebar;
    }

    public Customize sidebar(Boolean sidebar) {
        this.sidebar = sidebar;
        return this;
    }

    public void setSidebar(Boolean sidebar) {
        this.sidebar = sidebar;
    }

    public String getColor() {
        return color;
    }

    public Customize color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public Customize avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public Customize avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public String getDesktop() {
        return desktop;
    }

    public Customize desktop(String desktop) {
        this.desktop = desktop;
        return this;
    }

    public void setDesktop(String desktop) {
        this.desktop = desktop;
    }

    public User getUser() {
        return user;
    }

    public Customize user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public Customize projeto(Projeto projeto) {
        this.projeto = projeto;
        return this;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Cenario getCenario() {
        return cenario;
    }

    public Customize cenario(Cenario cenario) {
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
        Customize customize = (Customize) o;
        if (customize.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customize.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customize{" +
            "id=" + id +
            ", sidebar='" + sidebar + "'" +
            ", color='" + color + "'" +
            ", avatar='" + avatar + "'" +
            ", avatarContentType='" + avatarContentType + "'" +
            ", desktop='" + desktop + "'" +
            '}';
    }
}
