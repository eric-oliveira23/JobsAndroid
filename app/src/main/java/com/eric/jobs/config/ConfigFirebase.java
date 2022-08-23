package com.eric.jobs.config;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao;

    //retorna a instância
    public static FirebaseAuth getAutenticacao(){

        if (autenticacao == null){

            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;
    }

}
