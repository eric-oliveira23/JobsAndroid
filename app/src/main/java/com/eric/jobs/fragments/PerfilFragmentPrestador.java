package com.eric.jobs.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.eric.jobs.config.ConfigFirebase;
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerfilFragmentPrestador extends Fragment {

    private TextView txvCelular, txvEndereco, txvNome;
    private FirebaseAuth auth = ConfigFirebase.getAutenticacao();
    private DatabaseReference reference = ConfigFirebase.getReference();
    private ValueEventListener valueEventListenerUsuario, valueEventListenerProfile;
    private Button btnLogout;
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

        recuperarNome();
        recuperarImagens();

        txvCelular = getView().findViewById(R.id.txvCelular);
        txvEndereco = getView().findViewById(R.id.txvEndereco);
        txvNome = getView().findViewById(R.id.txvNome);
        imgBanner = getView().findViewById(R.id.imgBanner);
        imgPerfil = getView().findViewById(R.id.imgPerfil);

        btnLogout = getView().findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
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

    public void recuperarNome() {

        String userEmail = auth.getCurrentUser().getEmail();
        String userId = Base64Custom.codificarBase64(userEmail);

        DatabaseReference userReference = reference.child("prestadores")
                .child(userId);

        valueEventListenerUsuario = userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Prestador prestador = snapshot.getValue(Prestador.class);

                try {
                    txvCelular.setText(prestador.getCelular());
                    txvEndereco.setText(prestador.getCidade());
                    txvNome.setText(prestador.getNome());
                } catch (Exception exception) {

                    String userEmail = auth.getCurrentUser().getEmail();
                    String userId = Base64Custom.codificarBase64(userEmail);

                    DatabaseReference userReference = reference.child("usuarios")
                            .child(userId);

                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Usuario usuario = snapshot.getValue(Usuario.class);

                            txvNome.setText(usuario.getNome());

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
}


//consertar erro dos uploads das imagens e ver o pq as vezes nao vai e cai no catch do email válido

