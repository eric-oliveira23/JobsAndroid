package com.eric.jobs.fragments;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PesquisarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PesquisarFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recycler;
    private LottieAnimationView animationView;
//    private TextView txvSugestoes;
//    private Button btnEletricista, btnMarceneiro, btnEncanador;
    private ServicoAdapter servicoAdapter;
    private final List<Prestador> prestadors = new ArrayList<>();
    private DatabaseReference reference = ConfigFirebase.getReference();
    private DatabaseReference servicosRef;
    private ValueEventListener valueEventListenerServicos;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PesquisarFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PesquisarFragment newInstance(String param1, String param2) {
        PesquisarFragment fragment = new PesquisarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pesquisar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        animationView = view.findViewById(R.id.animationView);
//        txvSugestoes = view.findViewById(R.id.txvSugestoes);
//        btnEletricista = view.findViewById(R.id.btnEletricista);
//        btnMarceneiro = view.findViewById(R.id.btnMarceneiro);
//        btnEncanador = view.findViewById(R.id.btnEncanador);

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                hideViews();
                recycler.setVisibility(View.VISIBLE);
                return true;
            }
        });

        //recycler serviços
        recycler = getView().findViewById(R.id.recycler);

        //desativa o scroll do recycler
        //recyclerServicos.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(linearLayoutManager);

        servicoAdapter = new ServicoAdapter(prestadors, getActivity());
        recycler.setAdapter(servicoAdapter);

        recycler.setHasFixedSize(true);
        this.recuperarServicos();
    }

    private void filterList(String text) {
        List<Prestador> filteredList = new ArrayList<>();
        for (Prestador prestador : prestadors){
            if (prestador.getNome().toLowerCase().contains(text.toLowerCase()) ||
                    prestador.getCategoria().toLowerCase().contains(text.toLowerCase())) {

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

    public void hideViews(){

        animationView.setVisibility(View.GONE);
//        txvSugestoes.setVisibility(View.GONE);
//        btnEletricista.setVisibility(View.GONE);
//        btnEncanador.setVisibility(View.GONE);
//        btnMarceneiro.setVisibility(View.GONE);

    }

}