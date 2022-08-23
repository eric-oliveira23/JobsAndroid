package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eric.jobs.R;
import com.eric.jobs.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button BtnCriarConta;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnCriarConta = findViewById(R.id.BtnCriarConta);

    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarUserLogado();
    }

    public void VerificarUserLogado(){

        autenticacao = ConfigFirebase.getAutenticacao();

        if (autenticacao.getCurrentUser() != null){
            AbrirHome();
        }

    }

    public void CriarContaClicked(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void LoginClicked(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void AbrirHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}