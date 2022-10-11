package com.eric.jobs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.eric.jobs.R;
import com.eric.jobs.model.Prestador;
import com.eric.jobs.model.Servico;

import java.util.List;

public class ServicoAdapter extends RecyclerView.Adapter<ServicoAdapter.MyViewHolder>{

    List<Prestador> prestadors;
    Context context;

    public ServicoAdapter(List<Prestador> servicoList, FragmentActivity activity) {
        this.prestadors = servicoList;
        this.context = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Prestador prestador = prestadors.get(position);

        holder.tituloServico.setText(prestador.getNome());
        holder.categoriaServico.setText(prestador.getCategoria());
        holder.cidadeServico.setText(prestador.getCidade());
      // holder.imgServico.setImageResource(prestador.get);

    }

    @Override
    public int getItemCount() {
        return prestadors.size();
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
