package com.eric.jobs.model;

public class Destaque {

    private String tituloDestaque;
    private String categoriaDestaque;
    private String cidadeDestaque;
    private int imgDestaque;

    public Destaque(String destaque, String categoria, String cidade, int img) {

        this.tituloDestaque = destaque;
        this.categoriaDestaque = categoria;
        this.cidadeDestaque = cidade;
        this.imgDestaque = img;

    }

    public String getTituloDestaque() {
        return tituloDestaque;
    }

    public void setTituloDestaque(String tituloDestaque) {
        this.tituloDestaque = tituloDestaque;
    }

    public String getCategoriaDestaque() {
        return categoriaDestaque;
    }

    public void setCategoriaDestaque(String categoriaDestaque) {
        this.categoriaDestaque = categoriaDestaque;
    }

    public String getCidadeDestaque() {
        return cidadeDestaque;
    }

    public void setCidadeDestaque(String cidadeDestaque) {
        this.cidadeDestaque = cidadeDestaque;
    }

    public int getImgDestaque() {
        return imgDestaque;
    }

    public void setImgDestaque(int imgDestaque) {
        this.imgDestaque = imgDestaque;
    }
}
