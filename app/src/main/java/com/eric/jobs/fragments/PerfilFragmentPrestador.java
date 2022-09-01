package com.eric.jobs.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.eric.jobs.R;

import java.util.ArrayList;
import java.util.List;

public class PerfilFragmentPrestador extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //slider
        ImageSlider sliderServicos = getView().findViewById(R.id.sliderServicos);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.plumber));
        slideModels.add(new SlideModel(R.drawable.marceneiro));
        slideModels.add(new SlideModel(R.drawable.prestadorantena));
        slideModels.add(new SlideModel(R.drawable.prestadorantena));
        sliderServicos.setImageList(slideModels,false);

    }
}