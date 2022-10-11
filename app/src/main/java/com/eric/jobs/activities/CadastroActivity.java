package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eric.jobs.R;


public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }

    public void prestarServicoClick(View view){
        Intent intent = new Intent(this, CadastroTipoActivity.class);
        startActivity(intent);
    }

    public void loginClicked(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void btnUsuarioClicked(View view){
        Intent intent = new Intent(this, RegistroUsuarioActivity.class);
        startActivity(intent);
    }
}