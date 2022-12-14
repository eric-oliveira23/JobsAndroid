package com.eric.jobs.activities;

import static com.eric.jobs.helper.ValidarCNPJ.imprimeCNPJ;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eric.jobs.R;
import com.eric.jobs.helper.ValidarCNPJ;
import com.eric.jobs.helper.ValidarCPF;
import com.eric.jobs.services.ConfigFirebase;
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Prestador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RegistroPjActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView txvCategoria, txvCidade;
    private EditText edtNomeFantasia, edtCNPJ,
            edtEmail, edtSenha, edtConfirmarSenha,
             edtCelular, edtTelefoneFixo, edtInsta,
            edtFacebook;
    private Prestador prestador;
    private FirebaseAuth auth;
    private Uri profUri, bannerUri, servicoUri;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final DatabaseReference reference = ConfigFirebase.getReference();
    Dialog dialog;
    private String experiencia = "";
    private String cidade = "";
    private String categoria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pj);

        //categoria

        txvCategoria = findViewById(R.id.txvCategoria);
        txvCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia o dialog
                dialog = new Dialog(RegistroPjActivity.this);

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
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegistroPjActivity.this,
                        R.array.services,android.R.layout.simple_list_item_1);

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
                        txvCategoria.setText(adapter.getItem(position));
                        categoria = (String) adapter.getItem(position);

                        dialog.dismiss();
                    }
                });
            }
        });

        //        ---------------------------------
        //cidade
        txvCidade = findViewById(R.id.txvCidade);
        txvCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia o dialog
                dialog = new Dialog(RegistroPjActivity.this);

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

                // inicializa o arrayadapter
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegistroPjActivity.this,
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
                        //mostra a cidade selecionada no textview
                        txvCidade.setText(adapter.getItem(position));
                        cidade = (String) adapter.getItem(position);

                        dialog.dismiss();
                    }
                });
            }
        });

        //spinner tempo de experi??ncia
        Spinner spinExp = findViewById(R.id.spinExp);
        ArrayAdapter<CharSequence> adapterExp = ArrayAdapter.createFromResource(this,
                R.array.experience, android.R.layout.simple_spinner_item);

        adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinExp.setAdapter(adapterExp);
        spinExp.setOnItemSelectedListener(this);

        //        ---------------------------------

        Button btnPerfil = findViewById(R.id.btnPerfil);
        Button btnBanner = findViewById(R.id.btnBanner);
        Button btnImgServico = findViewById(R.id.btnImgServico);

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        btnImgServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 3);
            }
        });

        TextView btnLogar = findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        edtNomeFantasia = findViewById(R.id.edtNomeFantasia);
        edtCNPJ = findViewById(R.id.edtCNPJ);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenha);
        edtCelular = findViewById(R.id.edtCelular);
        edtTelefoneFixo = findViewById(R.id.edtTelefoneFixo);
        edtInsta = findViewById(R.id.edtInsta);
        edtFacebook = findViewById(R.id.edtFacebook);

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
                                "Voc?? pode escolher uma foto de perfil mais tarde", Toast.LENGTH_SHORT).show();
                    }
                    try{
                        uploadBanner();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),
                                "Voc?? pode escolher uma foto de capa mais tarde", Toast.LENGTH_SHORT).show();
                    }
                    try{
                        uploadImgServico();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),
                                "Voc?? pode escolher uma foto do servi??o mais tarde", Toast.LENGTH_SHORT).show();
                    }

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }

                else{
                    String excecao;
                    try{
                        throw Objects.requireNonNull(task.getException());
                    }
                    catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um email v??lido";
                    }catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Esse email j?? est?? sendo utilizado por outra pessoa!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usu??rio! "+e.getMessage();
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
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String senhaConfirmar = edtConfirmarSenha.getText().toString();
        String celular = edtCelular.getText().toString();
        String telefone = edtTelefoneFixo.getText().toString();
        String url_instagram = edtInsta.getText().toString();
        String url_facebook = edtFacebook.getText().toString();

        if (nome.isEmpty() || cidade.isEmpty() ||
                email.isEmpty() || senha.isEmpty() || senhaConfirmar.isEmpty() ||
                categoria.isEmpty() || experiencia.isEmpty() || celular.isEmpty() || cnpj.isEmpty()){

            Toast.makeText(RegistroPjActivity.this,
                    "Preencha os campos corretamente",
                    Toast.LENGTH_SHORT).show();

        }
        else {

            if (ValidarCNPJ.isCNPJ(cnpj)){

            if (!senha.equals(senhaConfirmar)) {
                Toast.makeText(RegistroPjActivity.this,
                        "As senhas n??o coincidem!",
                        Toast.LENGTH_SHORT).show();
            }

            else{

                prestador = new Prestador();
                prestador.setNome(nome);
                prestador.setDocumento(imprimeCNPJ(cnpj));
                prestador.setCidade(cidade);
                prestador.setEmail(email);
                prestador.setSenha(senha);
                prestador.setCategoria(categoria);
                prestador.setAno_experiencia(experiencia);
                prestador.setCelular(celular);
                prestador.setTelefone(telefone);
                prestador.setUrl_facebook(url_facebook);
                prestador.setUrl_instagram(url_instagram);
                prestador.setTipo("pj");

                cadastrarPrestador();

                }
            }else{
                Toast.makeText(RegistroPjActivity.this,
                        "Insira um CNPJ v??lido!",
                        Toast.LENGTH_SHORT).show();
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
        else if (requestCode==3 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
                servicoUri = data.getData();
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
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference profUrl = reference.child("prestadores").child(userId).
                                        child("img_perfil");
                                profUrl.setValue(uri.toString());
                            }
                        });
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
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        bannerRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference profUrl = reference.child("prestadores").child(userId).
                                        child("img_capa");
                                profUrl.setValue(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroPjActivity.this,
                                "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                    }
                });
            }

    private void uploadImgServico(){

        auth = ConfigFirebase.getAutenticacao();
        String userEmail = prestador.getEmail();
        String userId = Base64Custom.codificarBase64(userEmail);

        StorageReference imgServicoRef = storageReference.child("images/pic_servico/"+userId);

        imgServicoRef.putFile(servicoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgServicoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        DatabaseReference profUrl = reference.child("prestadores").child(userId).
                                                child("img_servico");
                                        profUrl.setValue(uri.toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegistroPjActivity.this,
                                                "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        experiencia  = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
