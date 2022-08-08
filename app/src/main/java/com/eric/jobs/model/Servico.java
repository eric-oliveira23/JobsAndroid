package com.eric.jobs.model;

public class Servico {

    private String tituloServico;
    private String categoriaServico;
    private String cidadeServico;
    private int imgServico;

    public Servico(String titulo, String categoria, String cidade, int img) {

        this.tituloServico = titulo;
        this.categoriaServico = categoria;
        this.cidadeServico = cidade;
        this.imgServico = img;

    }

    public String getTituloServico() {
        return tituloServico;
    }

    public void setTituloServico(String tituloServico) {
        this.tituloServico = tituloServico;
    }

    public String getCategoriaServico() {
        return categoriaServico;
    }

    public void setCategoriaServico(String categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    public String getCidadeServico() {
        return cidadeServico;
    }

    public void setCidadeServico(String cidadeServico) {
        this.cidadeServico = cidadeServico;
    }

    public int getImgServico() {
        return imgServico;
    }

    public void setImgServico(int imgServico) {
        this.imgServico = imgServico;
    }
}
