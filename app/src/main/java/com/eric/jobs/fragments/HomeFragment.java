package com.eric.jobs.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.eric.jobs.R;
import com.eric.jobs.activities.RegistroPjActivity;
import com.eric.jobs.adapter.DestaqueAdapter;
import com.eric.jobs.adapter.ServicoAdapter;
import com.eric.jobs.model.Destaque;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Usuario;
import com.eric.jobs.services.ConfigFirebase;
import com.eric.jobs.services.user.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private final UserRepository userRepository = new UserRepository();
    private RecyclerView recyclerDestaques, recyclerServicos;
    private final List<Destaque> destaques = new ArrayList<>();
    private final List<Prestador> prestadors = new ArrayList<>();
    private TextView txvWelcome, txvServicos, txvDestaques,
            txvJobs;
    private DatabaseReference reference = ConfigFirebase.getReference();
    private DatabaseReference servicosRef;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("image");
    private ValueEventListener valueEventListenerServicos;
    private ServicoAdapter servicoAdapter;
    private Button button;
    ImageSlider sliderDestaques;
    Animation main, txvs;
    Dialog dialog;
    String cidade;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

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

        observable();
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
        button = getView().findViewById(R.id.buttonFilter);
        txvServicos = getView().findViewById(R.id.txvServicos);
        txvDestaques = getView().findViewById(R.id.txvDestaques);
        txvJobs = getView().findViewById(R.id.txvJobs);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize dialog
                dialog = new Dialog(getActivity());

                // set custom dialog
                dialog.setContentView(R.layout.dialog_search);

                // set custom height and width
                dialog.getWindow().setLayout(650,800);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText=dialog.findViewById(R.id.edit_text);
                ListView listView=dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.cities,android.R.layout.simple_list_item_1);

                // set adapter
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
                        //mostra a cidade selecionado no textview
                        cidade = (String) adapter.getItem(position);
                        filterList(cidade);
                        dialog.dismiss();
                    }
                });
            }
        });

        //slider
        sliderDestaques = getView().findViewById(R.id.sliderDestaques);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.plumber));
        slideModels.add(new SlideModel(R.drawable.marceneiro));
        slideModels.add(new SlideModel(R.drawable.background));
        slideModels.add(new SlideModel(R.drawable.background));
        sliderDestaques.setImageList(slideModels,false);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Prestador prestador = dataSnapshot.getValue(Prestador.class);
                    prestadors.add(prestador);
                }
                servicoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        this.recuperarServicos();

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

    private void observable() {
        Observer<Usuario> usuarioObserver = new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable Usuario usuario) {
                if (usuario != null) {
                    txvWelcome.setText("Olá, " + usuario.getNome());
                }
            }
        };

        Observer<Prestador> prestadorObserver = new Observer<Prestador>() {
            @Override
            public void onChanged(@Nullable Prestador prestador) {
                if (prestador != null) {
                    txvWelcome.setText("Olá, " + prestador.getNome());
                }
            }
        };

        userRepository.getUsuario().observe(this, usuarioObserver);
        userRepository.getPrestador().observe(this, prestadorObserver);
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

    @Override
    public void onStop() {
        super.onStop();
        //reference.removeEventListener(valueEventListenerUsuario);
    }

    private void filterList(String text) {
        List<Prestador> filteredList = new ArrayList<>();
        for (Prestador prestador : prestadors){
            if (prestador.getCidade().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(prestador);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(getActivity(), "Nenhum prestador encontrado.", Toast.LENGTH_SHORT).show();
        }
        else {
            servicoAdapter.setFilteredList(filteredList);
        }

    }
}