package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.services.ConfigFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AlterarDadosPrestadorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView txvCategoria, txvCidade;
    EditText edtNomeAlterar, edtCelular, edtTelefoneFixo,
    edtFacebook, edtInstagram;
    Dialog dialog;
    String categoria = "";
    String experiencia = "";
    String cidade = "";
    private FirebaseAuth auth;
    private Uri profUri, bannerUri, servicoUri;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final DatabaseReference reference = ConfigFirebase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados_prestador);

        edtNomeAlterar = findViewById(R.id.edtNomeAlterar);
        edtCelular = findViewById(R.id.edtCelular);
        edtTelefoneFixo = findViewById(R.id.edtTelefoneFixo);
        edtFacebook = findViewById(R.id.edtFacebook);
        edtInstagram = findViewById(R.id.edtInsta);

        Button btnAlterarDados = findViewById(R.id.btnAlterarDados);
        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //categoria

        txvCategoria = findViewById(R.id.txvCategoria);
        txvCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia o dialog
                dialog = new Dialog(AlterarDadosPrestadorActivity.this);

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
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AlterarDadosPrestadorActivity.this,
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
                        //mostra a cidade selecionada no textview
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
                dialog = new Dialog(AlterarDadosPrestadorActivity.this);

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
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AlterarDadosPrestadorActivity.this,
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

        //spinner tempo de experiência
        Spinner spinExp = findViewById(R.id.spinExp);
        ArrayAdapter<CharSequence> adapterExp = ArrayAdapter.createFromResource(this,
                R.array.experience, android.R.layout.simple_spinner_item);

        adapterExp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinExp.setAdapter(adapterExp);
        spinExp.setOnItemSelectedListener(this);

        Button btnPerfil = findViewById(R.id.btnPerfil);
        Button btnBanner = findViewById(R.id.btnBanner);
        Button btnImgServico = findViewById(R.id.btnImgServico);

//        ------------------------------------------------------

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

        String userId = getUserId();

        StorageReference profileRef = storageReference.child("images/profile/"+userId+"pp");

        profileRef.putFile(profUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
                        Toast.makeText(AlterarDadosPrestadorActivity.this,
                                "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadBanner(){

        String userId = getUserId();

        StorageReference bannerRef = storageReference.child("images/banner/"+userId+"bp");

        bannerRef.putFile(bannerUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        bannerRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference bannerUrl = reference.child("prestadores").child(userId).
                                        child("img_capa");
                                bannerUrl.setValue(uri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AlterarDadosPrestadorActivity.this,
                                "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImgServico(){

        String userId = getUserId();

        StorageReference imgServicoRef = storageReference.child("images/pic_servico/"+userId);

        imgServicoRef.putFile(servicoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgServicoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        DatabaseReference imgServicoUrl = reference.child("prestadores").child(userId).
                                                child("img_servico");
                                        imgServicoUrl.setValue(uri.toString());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AlterarDadosPrestadorActivity.this,
                                                "Erro ao selecionar imagem", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                });
    }

    public void updateData(){

        String novoNome = edtNomeAlterar.getText().toString();
        String novaCidade = txvCidade.getText().toString();
        String novoCelular = edtCelular.getText().toString();
        String novoTelefone = edtTelefoneFixo.getText().toString();
        String novaCategoria = txvCategoria.getText().toString();
        String urlFacebook = edtFacebook.getText().toString();
        String urlInstagram = edtInstagram.getText().toString();

        if (novoNome.isEmpty() && cidade.isEmpty() && categoria.isEmpty()
                && experiencia.isEmpty() && novoCelular.isEmpty()
                && novoTelefone.isEmpty() && urlFacebook.isEmpty() &&
                urlInstagram.isEmpty() && profUri.equals(Uri.EMPTY) &&
                servicoUri.equals(Uri.EMPTY) && bannerUri.equals(Uri.EMPTY)
                && novaCategoria.isEmpty()) {

            Toast.makeText(AlterarDadosPrestadorActivity.this,
                    "Preencha ao menos um campo",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            auth = ConfigFirebase.getAutenticacao();
            String userEmail = auth.getCurrentUser().getEmail();
            String userId = Base64Custom.codificarBase64(userEmail);

            try {

                if (!novoNome.isEmpty()) {
                    DatabaseReference newNameRef = reference.child("prestadores").child(userId).
                            child("nome");
                    newNameRef.setValue(novoNome);
                }

                if (!cidade.isEmpty()) {
                    DatabaseReference newCityRef = reference.child("prestadores").child(userId).
                            child("cidade");
                    newCityRef.setValue(cidade);
                }

                if (!categoria.isEmpty()) {
                    DatabaseReference newCategoryRef = reference.child("prestadores").child(userId).
                            child("categoria");
                    newCategoryRef.setValue(categoria);
                }

                if (!experiencia.isEmpty()) {
                    DatabaseReference newExpRef = reference.child("prestadores").child(userId).
                            child("ano_experiencia");
                    newExpRef.setValue(experiencia);
                }

                if (!novoCelular.isEmpty()) {
                    DatabaseReference newCelRef = reference.child("prestadores").child(userId).
                            child("celular");
                    newCelRef.setValue(novoCelular);
                }

                if (!novoTelefone.isEmpty()) {
                    DatabaseReference newTelRef = reference.child("prestadores").child(userId).
                            child("telefone");
                    newTelRef.setValue(novoTelefone);
                }

                if (!urlInstagram.isEmpty()) {
                    DatabaseReference newInstaRef = reference.child("prestadores").child(userId).
                            child("url_instagram");
                    newInstaRef.setValue(urlInstagram);
                }

                if (!urlFacebook.isEmpty()) {
                    DatabaseReference newFaceRef = reference.child("prestadores").child(userId).
                            child("url_facebook");
                    newFaceRef.setValue(urlFacebook);
                }

                if (profUri != null){
                    uploadPic();
                }

                if (bannerUri != null){
                    uploadBanner();
                }

                if (servicoUri != null){
                    uploadImgServico();
                }

                Toast.makeText(AlterarDadosPrestadorActivity.this,
                        "Dados alterados.",
                        Toast.LENGTH_SHORT).show();

                finish();
            }

            catch (Exception ignored){}

        }
    }

    public String getUserId(){
        auth = ConfigFirebase.getAutenticacao();
        String userEmail = auth.getCurrentUser().getEmail();
        String userId = Base64Custom.codificarBase64(userEmail);
        return userId;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        experiencia  = adapterView.getItemAtPosition(i).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}