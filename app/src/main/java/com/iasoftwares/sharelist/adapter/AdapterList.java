package com.iasoftwares.sharelist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.model.ProdutosLista;
import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MyViewHolder> {

    List<ProdutosLista> movimentacoes;
    Context context;

    public AdapterList(List<ProdutosLista> movimentacoes, Context context) {
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProdutosLista movimentacao = movimentacoes.get(position);

    holder.nomeLista.setText(movimentacao.getKey());
    }


    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeLista;


        public MyViewHolder(View itemView) {
            super(itemView);

            nomeLista = itemView.findViewById(R.id.textAdapterNomeLista);

        }

    }

}
