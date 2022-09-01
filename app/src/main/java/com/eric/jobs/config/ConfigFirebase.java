package com.eric.jobs.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao;

    //retorna a instância do auth
    public static FirebaseAuth getAutenticacao(){

        if (autenticacao == null){

            autenticacao = FirebaseAuth.getInstance();

        }
        return autenticacao;
    }

    //retorna a instância do databaseref

    private static DatabaseReference reference;

    public static DatabaseReference getReference(){

        if (reference == null){
            reference = FirebaseDatabase.getInstance().getReference();
        }
        return reference;

    }

}
