package com.eric.jobs.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.eric.jobs.R;
import com.eric.jobs.activities.MainActivity;
import com.eric.jobs.adapter.DestaqueAdapter;
import com.eric.jobs.adapter.ServicoAdapter;
import com.eric.jobs.model.Destaque;
import com.eric.jobs.model.Servico;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerDestaques, recyclerServicos;
    private List<Destaque> destaques = new ArrayList<>();
    private List<Servico> servicos = new ArrayList<>();
    private Button btnDeslogar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDeslogar = view.findViewById(R.id.btnDeslogar);

        btnDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        //slider
        ImageSlider sliderDestaques = getView().findViewById(R.id.sliderDestaques);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.plumber));
        slideModels.add(new SlideModel(R.drawable.marceneiro));
        slideModels.add(new SlideModel(R.drawable.background));
        slideModels.add(new SlideModel(R.drawable.background));
        sliderDestaques.setImageList(slideModels,false);

        //recycler view destaques
        recyclerDestaques = getView().findViewById(R.id.recyclerDestaques);

        //define o layout
       /* RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerDestaques.setLayoutManager(layoutManager);*/

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerDestaques.setLayoutManager(layoutManager);

        this.PrepararDestaques();

        //define o adapter
        DestaqueAdapter destaqueAdapter = new DestaqueAdapter(destaques);
        recyclerDestaques.setAdapter(destaqueAdapter);

        recyclerDestaques.setHasFixedSize(true);

        //////////////////////////////////////////////////////////////

        //recycler serviços
        recyclerServicos = getView().findViewById(R.id.recyclerServicos);

        //desativa o scroll do recycler
        recyclerServicos.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerServicos.setLayoutManager(linearLayoutManager);

        this.PrepararServicos();

        ServicoAdapter servicoAdapter = new ServicoAdapter(servicos);
        recyclerServicos.setAdapter(servicoAdapter);

        recyclerServicos.setHasFixedSize(true);

    }

    public void PrepararDestaques(){

        Destaque d;
        d = new Destaque("Baroli Corretora","Corretora","Bocaina - SP",R.drawable.marceneiro);
        this.destaques.add(d);

        d = new Destaque("Baroli Corretora","Corretora","Bocaina - SP",R.drawable.plumber);
        this.destaques.add(d);

        d = new Destaque("Baroli Corretora","Corretora","Bocaina - SP",R.drawable.marceneiro);
        this.destaques.add(d);

        d = new Destaque("Baroli Corretora","Corretora","Bocaina - SP",R.drawable.plumber);
        this.destaques.add(d);

        d = new Destaque("Baroli Corretora","Corretora","Bocaina - SP",R.drawable.prestador);
        this.destaques.add(d);

    }

    public void PrepararServicos(){

        Servico s;
        s = new Servico("Mr. Plumber", "Encanador", "Bocaina - SP", R.drawable.prestador);
        this.servicos.add(s);

        s = new Servico("Mr. Plumber", "Teste", "Jaú - SP", R.drawable.plumber);
        this.servicos.add(s);

        s = new Servico("Mr. Plumber", "Encanador", "Bocaina - SP", R.drawable.marceneiro);
        this.servicos.add(s);

        s = new Servico("Mr. Plumber", "Encanador", "Bocaina - SP", R.drawable.plumber);
        this.servicos.add(s);

        s = new Servico("Mr. Plumber", "Encanador", "Bocaina - SP", R.drawable.prestador);
        this.servicos.add(s);

        s = new Servico("Mr. Plumber", "Encanador", "Bocaina - SP", R.drawable.plumber);
        this.servicos.add(s);

        s = new Servico("Mr. Plumber", "Encanador", "Bocaina - SP", R.drawable.marceneiro);
        this.servicos.add(s);

    }

}