package com.eric.jobs.model;

import com.eric.jobs.services.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String nome;
    private String email;
    private String cidade;
    private String senha;
    private String idUser;

    public void salvarUsuario(){

        DatabaseReference reference = ConfigFirebase.getReference();
        reference.child("usuarios")
                .child(this.idUser)
                .setValue(this);

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
