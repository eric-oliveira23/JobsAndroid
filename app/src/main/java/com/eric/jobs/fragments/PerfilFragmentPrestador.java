package com.eric.jobs.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.eric.jobs.R;
import com.eric.jobs.activities.MainActivity;
import com.eric.jobs.services.ConfigFirebase;
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Usuario;
import com.eric.jobs.services.user.UserRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerfilFragmentPrestador extends Fragment {

    private final UserRepository userRepository = new UserRepository();

    private ConstraintLayout constraintUser, constraintPrestador;
    private TextView txvCelular, txvEndereco, txvNome;
    private FirebaseAuth auth = ConfigFirebase.getAutenticacao();
    private DatabaseReference reference = ConfigFirebase.getReference();

    private Button btnLogout, btnLogoutUser;
    private StorageReference profReference = FirebaseStorage.getInstance().getReference();
    private StorageReference bannerReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgPerfil, imgBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observable();
        recuperarImagens();

        txvCelular = getView().findViewById(R.id.txvCelular);
        txvEndereco = getView().findViewById(R.id.txvEndereco);
        txvNome = getView().findViewById(R.id.txvNome);
        imgBanner = getView().findViewById(R.id.imgBanner);
        imgPerfil = getView().findViewById(R.id.imgPerfil);
        constraintUser = getView().findViewById(R.id.constraintUser);
        constraintPrestador = getView().findViewById(R.id.constraintPrestador);

        btnLogout = getView().findViewById(R.id.btnLogout);
        btnLogoutUser = getView().findViewById(R.id.btnLogoutUser);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        btnLogoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        //slider
        ImageSlider sliderServicos = getView().findViewById(R.id.sliderServicos);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.plumber));
        slideModels.add(new SlideModel(R.drawable.marceneiro));
        slideModels.add(new SlideModel(R.drawable.prestadorantena));
        slideModels.add(new SlideModel(R.drawable.prestadorantena));
        sliderServicos.setImageList(slideModels, false);

    }

    private void observable() {
        Observer<Usuario> usuarioObserver = new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable Usuario usuario) {
                txvNome.setText(usuario != null ? usuario.getNome() : "");
                constraintUser.setVisibility(View.VISIBLE);
                constraintPrestador.setVisibility(View.GONE);
            }
        };

        Observer<Prestador> prestadorObserver = new Observer<Prestador>() {
            @Override
            public void onChanged(@Nullable Prestador prestador) {
                if (prestador != null) {
                    constraintPrestador.setVisibility(View.VISIBLE);
                    constraintUser.setVisibility(View.GONE);
                    txvCelular.setText(prestador.getCelular());
                    txvEndereco.setText(prestador.getCidade());
                    txvNome.setText(prestador.getNome());
                }
            }
        };

        userRepository.getUsuario().observe(this, usuarioObserver);
        userRepository.getPrestador().observe(this, prestadorObserver);
    }


    public void recuperarImagens() {
        String email = auth.getCurrentUser().getEmail();
        String userId64 = Base64Custom.codificarBase64(email);
        StorageReference profileRef = profReference.child("images/profile/" + userId64 + "pp");

        try {
            final File localFilepp = File.createTempFile("prof", "jpg");
            profileRef.getFile(localFilepp)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmapProfile = BitmapFactory.decodeFile(localFilepp.getAbsolutePath());
                            imgPerfil.setImageBitmap(bitmapProfile);
                        }
                    });
        }catch (IOException e) {
            e.printStackTrace();
        }

        try {
            StorageReference bannerRef = bannerReference.child("images/banner/" + userId64 + "bp");

            final File localFilebp = File.createTempFile("bann", "jpg");
            bannerRef.getFile(localFilebp)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmapBanner = BitmapFactory.decodeFile(localFilebp.getAbsolutePath());
                            imgBanner.setImageBitmap(bitmapBanner);
                        }
                    });
            }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }


}