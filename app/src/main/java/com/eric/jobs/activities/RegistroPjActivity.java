package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.sax.StartElementListener;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class RegistroPjActivity extends AppCompatActivity {

    private TextView btnLogar;
    private EditText edtNomeFantasia, edtCNPJ, edtCidade,
            edtEmail, edtSenha, edtConfirmarSenha,
            edtCategoria, edtCelular, edtTelefoneFixo;
    private Prestador prestador;
    private FirebaseAuth auth;
    private Button btnPerfil, btnBanner;
    private Uri profUri, bannerUri;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String imageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pj);

        btnPerfil = findViewById(R.id.btnPerfil);
        btnBanner = findViewById(R.id.btnBanner);

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageType = "pp";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

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

                    try {
                        uploadPic();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),
                                "Você pode escolher uma foto de perfil mais tarde", Toast.LENGTH_SHORT).show();
                    }
                    try{
                        uploadBanner();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),
                                "Você pode escolher uma foto de capa mais tarde", Toast.LENGTH_SHORT).show();
                    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                profUri = data.getData();
            }
        else if (requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            bannerUri = data.getData();
        }
    }


    private void uploadPic(){

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Carregando...");
        pd.show();

        auth = ConfigFirebase.getAutenticacao();
        String userEmail = prestador.getEmail();
        String userId = Base64Custom.codificarBase64(userEmail);

        StorageReference profileRef = storageReference.child("images/profile/"+userId+"pp");

        profileRef.putFile(profUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
//                        Toast.makeText(RegistroPjActivity.this, "Imagem selecionada",
//                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(RegistroPjActivity.this,
                                "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressStatus = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progresso: "+ (int)progressStatus+"%");
                    }
                });
            }

    private void uploadBanner(){

        auth = ConfigFirebase.getAutenticacao();
        String userEmail = prestador.getEmail();
        String userId = Base64Custom.codificarBase64(userEmail);

        StorageReference bannerRef = storageReference.child("images/banner/"+userId+"bp");

        bannerRef.putFile(bannerUri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroPjActivity.this,
                                "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
