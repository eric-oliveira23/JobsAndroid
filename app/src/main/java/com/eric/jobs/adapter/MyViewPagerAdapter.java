package com.eric.jobs.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eric.jobs.config.ConfigFirebase;
import com.eric.jobs.fragments.HomeFragment;
import com.eric.jobs.fragments.PerfilFragmentPrestador;
import com.eric.jobs.fragments.PerfilFragmentUsuario;
import com.eric.jobs.fragments.PesquisarFragment;
import com.eric.jobs.helper.Base64Custom;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {

            case 1:
                return new PesquisarFragment();

            case 2:

                return new PerfilFragmentPrestador();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
