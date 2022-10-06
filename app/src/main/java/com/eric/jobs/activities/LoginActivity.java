package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eric.jobs.R;
import com.eric.jobs.config.ConfigFirebase;
import com.eric.jobs.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private Button btnUserLogar;
    private EditText edtEmail, edtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnUserLogar = findViewById(R.id.btnRegistrarPf);
        edtEmail = findViewById(R.id.edtCNPJ);
        edtSenha = findViewById(R.id.edtEmail);

        btnUserLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(LoginActivity.this,
                            "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
                }
                else {
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    ValidarUser();
                }

            }
        });

    }

    public void ValidarUser(){

        autenticacao = ConfigFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{

                    String excecao;
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email e senha não correspondem a um usuário cadastrado!";
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não cadastrado!";
                    }
                    catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário! "+e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void RegistrarClicked(View view){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

}