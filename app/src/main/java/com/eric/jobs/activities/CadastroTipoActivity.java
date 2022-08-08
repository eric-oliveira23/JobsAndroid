package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eric.jobs.R;

public class CadastroTipoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_tipo);
    }

    public void LoginClicked(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void PrestadorClicked(View view){
        Intent intent = new Intent(this, RegistroPfActivity.class);
        startActivity(intent);
    }

    public void EmpresaClicked(View view){
        Intent intent = new Intent(this, RegistroPjActivity.class);
        startActivity(intent);
    }

}