package com.eric.jobs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eric.jobs.R;
import com.eric.jobs.model.Destaque;

import java.util.List;

public class DestaqueAdapter extends RecyclerView.Adapter<DestaqueAdapter.MyViewHolder> {

    private List<Destaque> destaques;

    public DestaqueAdapter(List<Destaque> destaqueList){
        this.destaques = destaqueList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemdestaque = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destaques, parent, false);

        return new MyViewHolder(itemdestaque);
    }

    @Override
    public void onBindViewHolder(@NonNull DestaqueAdapter.MyViewHolder holder, int position) {

        Destaque destaque = destaques.get(position);

        holder.tituloDestaque.setText(destaque.getTituloDestaque());
        holder.cidadeDestaque.setText(destaque.getCidadeDestaque());
        holder.categoriaDestaque.setText(destaque.getCategoriaDestaque());
        holder.imgDestaque.setImageResource(destaque.getImgDestaque());

    }

    @Override
    public int getItemCount() {
        return destaques.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tituloDestaque;
        private TextView categoriaDestaque;
        private TextView cidadeDestaque;
        private ImageView imgDestaque;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tituloDestaque = itemView.findViewById(R.id.tituloDestaque);
            categoriaDestaque = itemView.findViewById(R.id.categoriaDestaque);
            cidadeDestaque = itemView.findViewById(R.id.cidadeDestaque);
            imgDestaque = itemView.findViewById(R.id.imgDestaque);
        }
    }

}
