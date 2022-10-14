package com.eric.jobs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eric.jobs.R;
import com.eric.jobs.services.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private Button BtnCriarConta;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        BtnCriarConta = findViewById(R.id.BtnCriarConta);

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_1)
                .backgroundDark(R.color.introbullet_light)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.purple_primary)
                .fragment(R.layout.intro_2)
                .backgroundDark(R.color.introbullet_purple)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.white)
                .fragment(R.layout.intro_3)
                .backgroundDark(R.color.introbullet_light)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.purple_primary)
                .fragment(R.layout.activity_main)
                .backgroundDark(R.color.introbullet_purple)
                .canGoForward(false)
                .build());

        setButtonNextVisible(false);
        setButtonBackVisible(false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUserLogado();
    }

    public void verificarUserLogado(){

        autenticacao = ConfigFirebase.getAutenticacao();

        if (autenticacao.getCurrentUser() != null){
            abrirHome();
        }

    }

    public void criarContaClicked(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void loginClicked(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void abrirHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}