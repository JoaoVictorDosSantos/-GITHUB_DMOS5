package br.edu.dmos5.github_dmos5.model;

import java.io.Serializable;

public class Repositorio implements Serializable {

    private Long id;
    private String name;
    private String html_url;

    public Repositorio(Long id, String name, String html_url) {
        this.id = id;
        this.name = name;
        this.html_url = html_url;
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

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }
}
