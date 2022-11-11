package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eric.jobs.R;
import com.eric.jobs.model.Prestador;

import java.util.Objects;

public class DetalhesPrestadorActivity extends AppCompatActivity {

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
    SwipeRefreshLayout swipeRefreshLayout;

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
        imgPerfil = findViewById(R.id.imgPerfil);
        imgBanner = findViewById(R.id.imgBanner);
        imgServicos = findViewById(R.id.imgServicos);
        txvCelular = findViewById(R.id.txvCelular);
        txvEndereco = findViewById(R.id.txvEndereco);
        txvServicosPrestados = findViewById(R.id.txvServicosPrestados);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();

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

}