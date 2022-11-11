package com.eric.jobs.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eric.jobs.R;
import com.eric.jobs.activities.AlterarDadosPrestadorActivity;
import com.eric.jobs.activities.AlterarDadosUserActivity;
import com.eric.jobs.activities.MainActivity;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Usuario;
import com.eric.jobs.services.user.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilFragment extends Fragment {

    private final UserRepository userRepository = new UserRepository();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintUser, constraintPrestador;
    private TextView txvCelular, txvEndereco, txvNome, txvCategoria, txvTempoExp,
            txvDetalhes, txvServicosPrestados;

    private ImageView imgPerfil, imgBanner, imgServicos;
    private LinearLayout linearTextoCategoria;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.perfil_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observable();

        txvCelular = getView().findViewById(R.id.txvCelular);
        swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        txvServicosPrestados = getView().findViewById(R.id.txvServicosPrestados);
        txvEndereco = getView().findViewById(R.id.txvEndereco);
        txvDetalhes = getView().findViewById(R.id.txvDetalhes);
        txvNome = getView().findViewById(R.id.txvNome);
        txvTempoExp = getView().findViewById(R.id.txvTempoExp);
        txvCategoria = getView().findViewById(R.id.txvCategoria);
        imgBanner = getView().findViewById(R.id.imgBanner);
        imgPerfil = getView().findViewById(R.id.imgPerfil);
        constraintUser = getView().findViewById(R.id.constraintUser);
        constraintPrestador = getView().findViewById(R.id.constraintPrestador);

        linearTextoCategoria = getView().findViewById(R.id.linearTextoCategoria);
        LinearLayout linearCategoria = getView().findViewById(R.id.linearCategoria);

        imgServicos = getView().findViewById(R.id.imgServicos);

        Button btnLogout = getView().findViewById(R.id.btnLogout);
        Button btnLogoutUser = getView().findViewById(R.id.btnLogoutUser);
        Button btnSettingsUser = getView().findViewById(R.id.btnSettingsUser);
        Button btnSettingsPrestador = getView().findViewById(R.id.btnSettingsPrestador);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                observable();
            }
        });

        btnSettingsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AlterarDadosUserActivity.class));
            }
        });

        btnSettingsPrestador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AlterarDadosPrestadorActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                PerfilFragment.this.signOut();
            }
        });

        btnLogoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view12) {
                PerfilFragment.this.signOut();
            }
        });

        linearCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view13) {
                if (linearTextoCategoria.getVisibility() == View.INVISIBLE) {
                    linearTextoCategoria.setVisibility(View.VISIBLE);
                    txvDetalhes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                            R.drawable.ic_baseline_arrow_drop_up_24, 0);
                } else {
                    linearTextoCategoria.setVisibility(View.INVISIBLE);
                    txvDetalhes.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                            R.drawable.ic_baseline_arrow_drop_down_24, 0);
                }
            }
        });
    }

    private void observable() {
        Observer<Usuario> usuarioObserver = new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable Usuario usuario) {
                constraintPrestador.setVisibility(View.GONE);
                constraintUser.setVisibility(View.VISIBLE);
                txvNome.setText(usuario != null ? usuario.getNome() : "");
            }
        };

        Observer<Prestador> prestadorObserver = new Observer<Prestador>() {
            @Override
            public void onChanged(@Nullable Prestador prestador) {
                if (prestador != null) {
                    constraintUser.setVisibility(View.GONE);
                    constraintPrestador.setVisibility(View.VISIBLE);
                    txvCelular.setText(prestador.getCelular());
                    txvEndereco.setText(prestador.getCidade());
                    txvNome.setText(prestador.getNome());
                    txvCategoria.setText(prestador.getCategoria());
                    txvTempoExp.setText(prestador.getAno_experiencia());

                    RequestOptions optionsProfile = new RequestOptions()
                            .placeholder(R.drawable.default_profile);

                    RequestOptions optionsBanner = new RequestOptions()
                            .placeholder(R.drawable.default_banner);

                    Glide.with(getActivity())
                            .load(prestador.getImg_perfil())
                            .apply(optionsProfile).into(imgPerfil);

                    Glide.with(getActivity())
                            .load(prestador.getImg_capa())
                            .apply(optionsBanner).into(imgBanner);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (prestador.getImg_servico().isEmpty()) {
                                imgServicos.setVisibility(View.GONE);
                                txvServicosPrestados.setVisibility(View.GONE);
                            }else {
                                Glide.with(getActivity())
                                        .load(prestador.getImg_servico())
                                        .apply(optionsBanner).into(imgServicos);
                            }
                        }
                    },4000);

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);

            }
        };

        userRepository.getUsuario().observe(getViewLifecycleOwner(), usuarioObserver);
        userRepository.getPrestador().observe(getViewLifecycleOwner(), prestadorObserver);
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }


}