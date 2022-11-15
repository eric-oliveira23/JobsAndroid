package com.eric.jobs.model;

import com.eric.jobs.services.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Prestador {

    private String nome = "";
    private String tipo = "";
    private String documento = "";
    private String cidade = "";
    private String email = "";
    private String senha = "";
    private String categoria = "";
    private String celular = "";
    private String telefone = "";
    private String img_perfil = "";
    private String img_capa = "";
    private String img_servico = "";
    private String idUser = "";
    private String ano_experiencia = "";
    private String url_instagram = "";
    private String url_facebook = "";

    public void salvarPrestador(){

        DatabaseReference reference = ConfigFirebase.getReference();
        reference.child("prestadores")
                .child(this.idUser)
                .setValue(this);

    }

    public String getUrl_instagram() {
        return url_instagram;
    }

    public void setUrl_instagram(String url_instagram) {
        this.url_instagram = url_instagram;
    }

    public String getUrl_facebook() {
        return url_facebook;
    }

    public void setUrl_facebook(String url_facebook) {
        this.url_facebook = url_facebook;
    }

    public String getAno_experiencia() {
        return ano_experiencia;
    }

    public void setAno_experiencia(String ano_experiencia) {
        this.ano_experiencia = ano_experiencia;
    }

    public String getImg_servico() {
        return img_servico;
    }

    public void setImg_servico(String img_servico) {
        this.img_servico = img_servico;
    }

    @Exclude
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getImg_perfil() {
        return img_perfil;
    }

    public void setImg_perfil(String img_perfil) {
        this.img_perfil = img_perfil;
    }
    
    public String getImg_capa() {
        return img_capa;
    }

    public void setImg_capa(String img_capa) {
        this.img_capa = img_capa;
    }
}
