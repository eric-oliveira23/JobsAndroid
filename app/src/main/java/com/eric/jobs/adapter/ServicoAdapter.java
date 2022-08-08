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
import com.eric.jobs.model.Servico;

import java.util.List;

public class ServicoAdapter extends RecyclerView.Adapter<ServicoAdapter.MyViewHolder>{

    private List<Servico> servicos;

    public ServicoAdapter(List<Servico> servicoList) {
        this.servicos = servicoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemservico = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.servicos, parent, false);

        return new MyViewHolder(itemservico);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicoAdapter.MyViewHolder holder, int position) {

        Servico servico = servicos.get(position);

        holder.tituloServico.setText(servico.getTituloServico());
        holder.categoriaServico.setText(servico.getCategoriaServico());
        holder.cidadeServico.setText(servico.getCidadeServico());
        holder.imgServico.setImageResource(servico.getImgServico());

    }

    @Override
    public int getItemCount() {
        return servicos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tituloServico;
        private TextView categoriaServico;
        private TextView cidadeServico;
        private ImageView imgServico;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tituloServico = itemView.findViewById(R.id.tituloServico);
            categoriaServico = itemView.findViewById(R.id.categoriaServico);
            cidadeServico = itemView.findViewById(R.id.cidadeServico);
            imgServico = itemView.findViewById(R.id.imgServico);
        }
    }

}
