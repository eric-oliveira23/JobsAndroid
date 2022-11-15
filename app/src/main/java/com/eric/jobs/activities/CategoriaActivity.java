package com.eric.jobs.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.eric.jobs.R;
import com.eric.jobs.adapter.ServicoAdapter;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.services.ConfigFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriaActivity extends AppCompatActivity {

    private RecyclerView recyclerServicos;
    private LottieAnimationView animLoading;
    private ServicoAdapter servicoAdapter;
    private final List<Prestador> prestadors = new ArrayList<>();
    private final DatabaseReference reference = ConfigFirebase.getReference();
    private ServicoAdapter.RecyclerViewClickListener listener;
    private TextView txvFiltroCategoria;
    String categoria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        txvFiltroCategoria = findViewById(R.id.txvFiltroCategoria);
        animLoading = findViewById(R.id.animLoading);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoria = extras.getString("nome");
        }

        txvFiltroCategoria.setText(categoria);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                filterList(categoria);
                animLoading.setVisibility(View.GONE);
                recyclerServicos.setVisibility(View.VISIBLE);
            }
        }, 1000);

        //-----------------------------------------RECYCLER SERVIÇOS----------------------------------------

        //recycler serviços
        recyclerServicos = findViewById(R.id.recyclerServicos);

        this.recuperarServicos();

        //desativa o scroll do recycler
        //recyclerServicos.setNestedScrollingEnabled(false);
        setOnClickListener();
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerServicos.setLayoutManager(linearLayoutManager);

        servicoAdapter = new ServicoAdapter(prestadors, this, listener);
        recyclerServicos.setAdapter(servicoAdapter);

        recyclerServicos.setHasFixedSize(true);

    }

    public void setOnClickListener(){
        listener = new ServicoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), DetalhesPrestadorActivity.class);
                intent.putExtra("nome", servicoAdapter.getItem(position).getNome());
                intent.putExtra("categoria", servicoAdapter.getItem(position).getCategoria());
                intent.putExtra("cidade", servicoAdapter.getItem(position).getCidade());
                intent.putExtra("perfil", servicoAdapter.getItem(position).getImg_perfil());
                intent.putExtra("celular", servicoAdapter.getItem(position).getCelular());
                intent.putExtra("perfil", servicoAdapter.getItem(position).getImg_perfil());
                intent.putExtra("banner", servicoAdapter.getItem(position).getImg_capa());
                intent.putExtra("img_servico", servicoAdapter.getItem(position).getImg_servico());
                intent.putExtra("experiencia", servicoAdapter.getItem(position).getAno_experiencia());
                intent.putExtra("url_facebook", servicoAdapter.getItem(position).getUrl_facebook());
                intent.putExtra("url_instagram", servicoAdapter.getItem(position).getUrl_instagram());

                startActivity(intent);
            }
        };
    }

    public void recuperarServicos(){

        DatabaseReference servicosRef = reference.child("prestadores");

        servicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                prestadors.clear();

                for (DataSnapshot dados : snapshot.getChildren()) {

                    Prestador prestador = dados.getValue(Prestador.class);
                    prestadors.add(prestador);

                }
                servicoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void filterList(String text) {
        List<Prestador> filteredList = new ArrayList<>();
        for (Prestador prestador : prestadors){
            if (prestador.getCategoria().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(prestador);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Nenhum prestador encontrado.", Toast.LENGTH_SHORT).show();
        }
        else {
            servicoAdapter.setFilteredList(filteredList);
        }

    }

}