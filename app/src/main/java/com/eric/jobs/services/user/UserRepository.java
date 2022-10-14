package com.eric.jobs.services.user;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Usuario;
import com.eric.jobs.services.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserRepository {

    private final DatabaseReference reference = ConfigFirebase.getReference();
    private final FirebaseAuth auth = ConfigFirebase.getAutenticacao();
    private final String userEmail = auth.getCurrentUser().getEmail();
    private final String userId = Base64Custom.codificarBase64(userEmail);

    private final MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    private final MutableLiveData<Prestador> prestador = new MutableLiveData<>();

    public MutableLiveData<Usuario> getUsuario() {
        try {
            reference.child("usuarios").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usuario.postValue(snapshot.getValue(Usuario.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return usuario;
        } catch (Exception ignored) {
        }

        return null;
    }

    public MutableLiveData<Prestador> getPrestador() {
        try {
            reference.child("prestadores").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    prestador.postValue(snapshot.getValue(Prestador.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return prestador;
        } catch (Exception ignored) {
        }

        return null;
    }
}
