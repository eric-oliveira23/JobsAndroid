package com.eric.jobs.model;

import android.content.Context;

import com.eric.jobs.adapter.ServicoAdapter;

public class Destaque {

    private String tituloDestaque;
    private int imgDestaque;

    public Destaque(String destaque, int img) {

        this.tituloDestaque = destaque;
        this.imgDestaque = img;

    }

    public String getTituloDestaque() {
        return tituloDestaque;
    }

    public void setTituloDestaque(String tituloDestaque) {
        this.tituloDestaque = tituloDestaque;
    }

    public int getImgDestaque() {
        return imgDestaque;
    }

    public void setImgDestaque(int imgDestaque) {
        this.imgDestaque = imgDestaque;
    }
}
