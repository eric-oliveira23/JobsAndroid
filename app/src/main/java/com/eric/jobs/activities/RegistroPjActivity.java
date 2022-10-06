package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.jobs.R;
import com.eric.jobs.config.ConfigFirebase;
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Prestador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistroPjActivity extends AppCompatActivity {

    private TextView btnLogar;
    private EditText edtNomeFantasia, edtCNPJ, edtCidade,
            edtEmail, edtSenha, edtConfirmarSenha,
            edtCategoria, edtCelular, edtTelefoneFixo;
    private Prestador prestador;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pj);

        btnLogar = findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnLogar = findViewById(R.id.btnLogar);

        edtNomeFantasia = findViewById(R.id.edtNomeFantasia);
        edtCNPJ = findViewById(R.id.edtCNPJ);
        edtCidade = findViewById(R.id.edtCidade);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenha);
        edtCategoria = findViewById(R.id.edtCategoria);
        edtCelular = findViewById(R.id.edtCelular);
        edtTelefoneFixo = findViewById(R.id.edtTelefoneFixo);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void cadastrarPrestador(){

        auth = ConfigFirebase.getAutenticacao();
        auth.createUserWithEmailAndPassword(
                prestador.getEmail(), prestador.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String idUser = Base64Custom.codificarBase64(prestador.getEmail());
                    prestador.setIdUser(idUser);
                    prestador.salvarPrestador();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }

                else{

                    String excecao;
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um email válido";
                    }catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Esse email já está sendo utilizado por outra pessoa!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário! "+e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(),
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void btnCadastrar(View v) {

        String nome = edtNomeFantasia.getText().toString();
        String cnpj = edtCNPJ.getText().toString();
        String cidade = edtCidade.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String senhaConfirmar = edtConfirmarSenha.getText().toString();
        String categoria = edtCategoria.getText().toString();
        String celular = edtCelular.getText().toString();
        String telefone = edtTelefoneFixo.getText().toString();

        if (nome.isEmpty() || cidade.isEmpty() ||
                email.isEmpty() || senha.isEmpty() || senhaConfirmar.isEmpty() ||
                categoria.isEmpty() || celular.isEmpty() || cnpj.isEmpty()){

            Toast.makeText(RegistroPjActivity.this,
                    "Preencha os campos corretamente",
                    Toast.LENGTH_SHORT).show();

        }
        else {

            if (!senha.equals(senhaConfirmar)) {
                Toast.makeText(RegistroPjActivity.this,
                        "As senhas não coincidem!",
                        Toast.LENGTH_SHORT).show();
            }

            else{

                prestador = new Prestador();
                prestador.setNome(nome);
                prestador.setDocumento(cnpj);
                prestador.setCidade(cidade);
                prestador.setEmail(email);
                prestador.setSenha(senha);
                prestador.setCategoria(categoria);
                prestador.setCelular(celular);
                prestador.setTelefone(telefone);
                prestador.setTipo("pj");

                cadastrarPrestador();
            }
        }
    }
}
