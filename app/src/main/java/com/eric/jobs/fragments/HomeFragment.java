package com.eric.jobs.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.eric.jobs.R;
import com.eric.jobs.adapter.DestaqueAdapter;
import com.eric.jobs.adapter.ServicoAdapter;
import com.eric.jobs.config.ConfigFirebase;
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Destaque;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Servico;
import com.eric.jobs.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseAuth auth = ConfigFirebase.getAutenticacao();
    private DatabaseReference reference = ConfigFirebase.getReference();
    private DatabaseReference servicosRef;
    private RecyclerView recyclerDestaques, recyclerServicos;
    private List<Destaque> destaques = new ArrayList<>();
    private List<Prestador> prestadors = new ArrayList<>();
    private TextView txvWelcome, txvServicos, txvDestaques,
            txvJobs;
    private ValueEventListener valueEventListenerUsuario, valueEventListenerPrestador, valueEventListenerServicos;
    private ServicoAdapter servicoAdapter;
    private Button button;
    ImageSlider sliderDestaques;
    Animation main, txvs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarNome();

        main = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_home);
        txvs = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_home_txv);
        recyclerDestaques.startAnimation(main);
        recyclerServicos.startAnimation(main);
        button.startAnimation(main);
        sliderDestaques.startAnimation(main);
        txvServicos.startAnimation(main);
        txvDestaques.startAnimation(main);

        txvWelcome.startAnimation(txvs);
        txvJobs.startAnimation(txvs);

        this.recuperarServicos();
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

        txvWelcome = getView().findViewById(R.id.txvWelcome);
        button = getView().findViewById(R.id.button);
        txvServicos = getView().findViewById(R.id.txvServicos);
        txvDestaques = getView().findViewById(R.id.txvDestaques);
        txvJobs = getView().findViewById(R.id.txvJobs);

        //slider
        sliderDestaques = getView().findViewById(R.id.sliderDestaques);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.plumber));
        slideModels.add(new SlideModel(R.drawable.marceneiro));
        slideModels.add(new SlideModel(R.drawable.background));
        slideModels.add(new SlideModel(R.drawable.background));
        sliderDestaques.setImageList(slideModels,false);

//-----------------------------------------RECYCLER DESTAQUES----------------------------------------

        //recycler view destaques
        recyclerDestaques = getView().findViewById(R.id.recyclerDestaques);

        //define o layout
       /* RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerDestaques.setLayoutManager(layoutManager);*/

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerDestaques.setLayoutManager(layoutManager);

        this.prepararDestaques();

        //define o adapter
        DestaqueAdapter destaqueAdapter = new DestaqueAdapter(destaques);
        recyclerDestaques.setAdapter(destaqueAdapter);

        recyclerDestaques.setHasFixedSize(true);

//-----------------------------------------RECYCLER SERVIÇOS----------------------------------------

        //recycler serviços
        recyclerServicos = getView().findViewById(R.id.recyclerServicos);

        //desativa o scroll do recycler
        //recyclerServicos.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerServicos.setLayoutManager(linearLayoutManager);

        servicoAdapter = new ServicoAdapter(prestadors, getActivity());
        recyclerServicos.setAdapter(servicoAdapter);

        recyclerServicos.setHasFixedSize(true);

    }

    public void prepararDestaques(){

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

    public void recuperarServicos(){

        servicosRef = reference.child("prestadores");

        valueEventListenerServicos = servicosRef.addValueEventListener(new ValueEventListener() {
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

    public void recuperarNome() {

            String userEmail = auth.getCurrentUser().getEmail();
            String userId = Base64Custom.codificarBase64(userEmail);

            DatabaseReference userReference = reference.child("usuarios")
                    .child(userId);

            valueEventListenerUsuario = userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario usuario = snapshot.getValue(Usuario.class);

                    try {
                        txvWelcome.setText("Olá, " + usuario.getNome());
                    }
                         catch (Exception exception) {

                              DatabaseReference prestadorReference = reference.child("prestadores")
                                     .child(userId);

                             valueEventListenerPrestador = prestadorReference.addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                     Prestador prestador = snapshot.getValue(Prestador.class);
                                     txvWelcome.setText("Olá, " + prestador.getNome());
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError error) {

                                 }
                             });
                        }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    @Override
    public void onStop() {
        super.onStop();
        //reference.removeEventListener(valueEventListenerUsuario);
    }
}