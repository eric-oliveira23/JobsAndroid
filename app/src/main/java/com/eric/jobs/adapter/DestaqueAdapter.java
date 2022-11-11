package com.eric.jobs.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eric.jobs.R;
import com.eric.jobs.model.Destaque;
import com.eric.jobs.model.Prestador;

import java.util.List;

public class DestaqueAdapter extends RecyclerView.Adapter<DestaqueAdapter.MyViewHolder> {

    private List<Destaque> destaques;
    private RecyclerViewClickListener listener;
    private Context context;

    public DestaqueAdapter(List<Destaque> destaqueList, Activity activity, RecyclerViewClickListener listener){
        this.destaques = destaqueList;
        this.listener = listener;
        this.context = activity;
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
        holder.imgDestaque.setImageResource(destaque.getImgDestaque());

    }

    public Destaque getItem (int position) {
        return destaques.get(position);
    }

    @Override
    public int getItemCount() {
        return destaques.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView tituloDestaque;
        private final ImageView imgDestaque;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tituloDestaque = itemView.findViewById(R.id.tituloDestaque);
            imgDestaque = itemView.findViewById(R.id.imgDestaque);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(itemView, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

}
