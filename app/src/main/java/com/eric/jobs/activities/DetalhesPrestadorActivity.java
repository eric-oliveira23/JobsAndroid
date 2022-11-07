package com.eric.jobs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_prestador);

        LinearLayout linearCategoria = findViewById(R.id.linearCategoria);
        LinearLayout linearTextoCategoria = findViewById(R.id.linearTextoCategoria);
        TextView txvNome = findViewById(R.id.txvNome);
        TextView txvDetalhes = findViewById(R.id.txvDetalhes);
        TextView txvCategoria = findViewById(R.id.txvCategoria);
        TextView txvTempoExp = findViewById(R.id.txvTempoExp);
        ImageView imgPerfil = findViewById(R.id.imgPerfil);
        ImageView imgBanner = findViewById(R.id.imgBanner);
        ImageView imgServicos = findViewById(R.id.imgServicos);
        TextView txvCelular = findViewById(R.id.txvCelular);
        TextView txvEndereco = findViewById(R.id.txvEndereco);
        TextView txvServicosPrestados = findViewById(R.id.txvServicosPrestados);

        String nome = "";
        String categoria = "";
        String cidade = "";
        String celular = "";
        String banner = "";
        String perfil = "";
        String exp = "";
        String img_servico = "";

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
}