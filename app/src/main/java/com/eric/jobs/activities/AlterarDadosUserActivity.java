package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

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
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Usuario;
import com.eric.jobs.services.ConfigFirebase;
import com.eric.jobs.services.user.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class AlterarDadosUserActivity extends AppCompatActivity {

    private String cidade = "";
    private Dialog dialog;
    private TextView txvCidade;
    private final DatabaseReference reference = ConfigFirebase.getReference();
    private FirebaseAuth auth;
    private final UserRepository userRepository = new UserRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados_user);

        EditText edtNomeAlterar = findViewById(R.id.edtNomeAlterar);

        Button btnAlterarDados = findViewById(R.id.btnAlterarDados);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        txvCidade = findViewById(R.id.txvCidade);

        txvCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inicia o dialog
                dialog = new Dialog(AlterarDadosUserActivity.this);

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
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AlterarDadosUserActivity.this,
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

        btnAlterarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String novoNome = edtNomeAlterar.getText().toString();

                if (novoNome.isEmpty() && cidade.isEmpty()){
                    Toast.makeText(AlterarDadosUserActivity.this,
                            "Preencha ao menos um campo",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    auth = ConfigFirebase.getAutenticacao();
                    String userEmail = auth.getCurrentUser().getEmail();
                    String userId = Base64Custom.codificarBase64(userEmail);

                    try {

                        if (!novoNome.isEmpty()) {
                            DatabaseReference newNameRef = reference.child("usuarios").child(userId).
                                    child("nome");
                            newNameRef.setValue(novoNome);
                        }

                        if (!cidade.isEmpty()) {
                            DatabaseReference newCityRef = reference.child("usuarios").child(userId).
                                    child("cidade");
                            newCityRef.setValue(cidade);
                        }

                        Toast.makeText(AlterarDadosUserActivity.this,
                                "Dados alterados.",
                                Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception ignored){}

                    finish();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}