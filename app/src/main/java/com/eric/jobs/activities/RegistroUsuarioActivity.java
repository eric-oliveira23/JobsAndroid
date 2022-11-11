package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.jobs.R;
import com.eric.jobs.services.ConfigFirebase;
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
            edtSenhaUsuario, edtConfirmarSenha;
    private Dialog dialog;
    private TextView txvCidade;
    String cidade;
    private Button btnRegistrarUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        edtNomeCompleto = findViewById(R.id.edtNomeFantasia);
        edtEmail = findViewById(R.id.edtCNPJ);
        edtSenhaUsuario = findViewById(R.id.edtEmail);
        edtConfirmarSenha = findViewById(R.id.edtSenha);
        txvCidade = findViewById(R.id.txvCidade);
        btnRegistrarUser = findViewById(R.id.btnRegistrarUser);

        txvCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia o dialog
                dialog = new Dialog(RegistroUsuarioActivity.this);

                // define o dialog customizado
                dialog.setContentView(R.layout.dialog_search);

                // define altura e largura
                dialog.getWindow().setLayout(650,800);

                // define o background tranparente
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // exibe o dialog
                dialog.show();

                // inicializa as variaveis dos componentes do dialog
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // inicaliza o arrayadapter
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegistroUsuarioActivity.this,
                        R.array.cities,android.R.layout.simple_list_item_1);

                // seta o adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //mostra a categoria selecionada no textview
                        txvCidade.setText(adapter.getItem(position));
                        cidade = (String) adapter.getItem(position);

                        dialog.dismiss();
                    }
                });
            }
        });

        btnRegistrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = edtNomeCompleto.getText().toString();
                String email = edtEmail.getText().toString();
                String cidade = txvCidade.getText().toString();
                String senha = edtSenhaUsuario.getText().toString();
                String confirmarSenha = edtConfirmarSenha.getText().toString();

                if (nome.isEmpty() || email.isEmpty() ||
                        cidade.isEmpty() || senha.isEmpty() ||
                        confirmarSenha.isEmpty()){

                    Toast.makeText(getApplicationContext(),
                            "Preencha os campos corretamente!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    if (!senha.equals(confirmarSenha)){
                        Toast.makeText(getApplicationContext(),
                                "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setCidade(cidade);
                        usuario.setSenha(senha);

                        cadastrarUser();

                    }
            }
        }

    public void cadastrarUser(){

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
        });
    }

    public void loginClicked(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

}
