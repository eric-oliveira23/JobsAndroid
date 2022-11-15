package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eric.jobs.R;
import com.eric.jobs.services.ConfigFirebase;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class DetalhesPrestadorActivity extends AppCompatActivity implements View.OnClickListener {

    private String nome = "";
    private String categoria = "";
    private String cidade = "";
    private String celular = "";
    private String banner = "";
    private String perfil = "";
    private String exp = "";
    private String img_servico = "";
    private TextView txvNome;
    private TextView txvDetalhes;
    private TextView txvCategoria;
    private TextView txvTempoExp;
    private ImageView imgPerfil;
    private ImageView imgBanner;
    private ImageView imgServicos;
    private TextView txvCelular;
    private TextView txvEndereco;
    private TextView txvServicosPrestados;
    private TextView txvInstagram, txvFacebook;
    private FloatingActionButton fabContato, fabEmail;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseAuth auth = ConfigFirebase.getAutenticacao();
    String email = auth.getCurrentUser().getEmail();
    String urlInsta = "";
    String urlFace = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_prestador);

        LinearLayout linearCategoria = findViewById(R.id.linearCategoria);
        LinearLayout linearTextoCategoria = findViewById(R.id.linearTextoCategoria);
        txvNome = findViewById(R.id.txvNome);
        txvDetalhes = findViewById(R.id.txvDetalhes);
        txvCategoria = findViewById(R.id.txvCategoria);
        txvTempoExp = findViewById(R.id.txvTempoExp);
        txvInstagram = findViewById(R.id.txvInstagram);
        txvFacebook = findViewById(R.id.txvFacebook);
        txvCelular = findViewById(R.id.txvCelular);
        txvEndereco = findViewById(R.id.txvEndereco);
        txvServicosPrestados = findViewById(R.id.txvServicosPrestados);

        imgPerfil = findViewById(R.id.imgPerfil);
        imgBanner = findViewById(R.id.imgBanner);
        imgServicos = findViewById(R.id.imgServicos);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        fabContato = findViewById(R.id.fabContato);
        fabEmail = findViewById(R.id.fabEmail);

        getData();

        txvNome.setOnClickListener(this);
        txvCelular.setOnClickListener(this);
        txvFacebook.setOnClickListener(this);
        txvInstagram.setOnClickListener(this);

        fabContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email)

                        .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,
                                ContactsContract.CommonDataKinds.Email.TYPE_WORK)

                        .putExtra(ContactsContract.Intents.Insert.PHONE, celular)

                        .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK)

                        .putExtra(ContactsContract.Intents.Insert.NAME, nome);

                startActivity(intent);
            }
        });

        fabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Trabalho");

                startActivity(Intent.createChooser(emailIntent, "Enviar Email"));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        linearCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearTextoCategoria.getVisibility() == View.INVISIBLE){
                    linearTextoCategoria.setVisibility(View.VISIBLE);
                    txvDetalhes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                            R.drawable.ic_baseline_arrow_drop_up_24, 0);
                }else {
                    linearTextoCategoria.setVisibility(View.INVISIBLE);
                    txvDetalhes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                            R.drawable.ic_baseline_arrow_drop_down_24, 0);
                }
            }
        });
    }

    public void getData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            nome = extras.getString("nome");
            categoria = extras.getString("categoria");
            cidade = extras.getString("cidade");
            celular = extras.getString("celular");
            banner = extras.getString("banner");
            perfil = extras.getString("perfil");
            exp = extras.getString("experiencia");
            img_servico = extras.getString("img_servico");
            urlInsta = extras.getString("url_instagram");
            urlFace = extras.getString("url_facebook");
        }

        txvNome.setText(nome);
        txvCategoria.setText(categoria);
        txvCelular.setText(celular);
        txvEndereco.setText(cidade);
        txvTempoExp.setText(exp);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.default_profile);

        Glide.with(this).load(banner).apply(options).into(imgBanner);
        Glide.with(this).load(perfil).apply(options).into(imgPerfil);

        if (urlInsta.isEmpty())
            txvInstagram.setVisibility(View.GONE);

        if (urlFace.isEmpty())
            txvFacebook.setVisibility(View.GONE);

        if (img_servico.isEmpty()) {
            imgServicos.setVisibility(View.GONE);
            txvServicosPrestados.setVisibility(View.GONE);
        }else {
            Glide.with(this).load(img_servico).apply(options).into(imgServicos);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        },2000);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.txvNome:
                clipData("nome", nome);
                showCopiedMessage();
                break;

            case R.id.txvCelular:
                clipData("celular", celular);
                showCopiedMessage();
                break;

            case R.id.txvFacebook:
                if (urlFace.startsWith("https://") || urlFace.startsWith("http://")) {
                    Uri uri = Uri.parse(urlFace);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Link inválido", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txvInstagram:
                if (urlInsta.startsWith("https://") || urlInsta.startsWith("http://")) {
                    Uri uri = Uri.parse(urlInsta);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Link inválido", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    public void showCopiedMessage(){
        Toast.makeText(this,
                "Copiado para a área de transferência.",
                Toast.LENGTH_SHORT).show();
    }

    public void clipData(String label, String message){
        ClipboardManager clipNome = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, message);
        clipNome.setPrimaryClip(clip);
    }

}