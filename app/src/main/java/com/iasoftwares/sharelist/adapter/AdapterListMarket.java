package com.iasoftwares.sharelist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.model.ProdutosLista;
import java.util.List;

public class AdapterListMarket extends RecyclerView.Adapter<AdapterListMarket.MyViewHolder> {

    List<ProdutosLista> movimentacoes;
    Context context;
    OnClick onClick;

    public AdapterListMarket(List<ProdutosLista> movimentacoes, Context context, OnClick onClick) {
        this.movimentacoes = movimentacoes;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_market_list, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProdutosLista movimentacao = movimentacoes.get(position);

        holder.deleteListImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.DeletaItem(position);
            }
        });
        holder.selectListImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.EscolheLista(position);
            }
        });

        holder.nomeLista.setText(movimentacao.getKey());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.EscolheLista(position);
            }
        });
    }





    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeLista;
        ImageView selectListImg, deleteListImg;


        public MyViewHolder(View itemView) {
            super(itemView);

            nomeLista = itemView.findViewById(R.id.textAdapterNomeLista);
            selectListImg = itemView.findViewById(R.id.selectIDList);
            deleteListImg = itemView.findViewById(R.id.deleteIDList);


        }

    }

}
