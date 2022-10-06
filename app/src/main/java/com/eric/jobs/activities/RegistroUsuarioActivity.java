package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eric.jobs.R;
import com.eric.jobs.config.ConfigFirebase;
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private EditText edtNomeCompleto, edtEmail,
            edtCidade, edtSenhaUsuario,
            edtConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        edtNomeCompleto = findViewById(R.id.edtNomeFantasia);
        edtEmail = findViewById(R.id.edtCNPJ);
        edtCidade = findViewById(R.id.edtCidade);
        edtSenhaUsuario = findViewById(R.id.edtEmail);
        edtConfirmarSenha = findViewById(R.id.edtSenha);
    }

    public void btnLogin(View view){

        String nome = edtNomeCompleto.getText().toString();
        String email = edtEmail.getText().toString();
        String cidade = edtCidade.getText().toString();
        String senha = edtSenhaUsuario.getText().toString();
        String confirmarSenha = edtConfirmarSenha.getText().toString();

       if (nome.isEmpty() || email.isEmpty() ||
               cidade.isEmpty() || senha.isEmpty() ||
               confirmarSenha.isEmpty()){

           Toast.makeText(this,
                   "Preencha os campos corretamente!",
                   Toast.LENGTH_SHORT).show();
       }
       else{
                if (!senha.equals(confirmarSenha)){
                    Toast.makeText(this,
                            "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                }

           else{

               usuario = new Usuario();
               usuario.setNome(nome);
               usuario.setEmail(email);
               usuario.setCidade(cidade);
               usuario.setSenha(senha);

               CadastrarUser();
           }
       }

    }

    public void CadastrarUser(){

        autenticacao = ConfigFirebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String idUser = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUser(idUser);
                    usuario.salvarUsuario();

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
}