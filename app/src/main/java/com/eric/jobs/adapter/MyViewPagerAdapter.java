package com.eric.jobs.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.eric.jobs.fragments.HomeFragment;
import com.eric.jobs.fragments.PerfilFragmentPrestador;
import com.eric.jobs.fragments.PesquisarFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            case 0:
                return new HomeFragment();

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
