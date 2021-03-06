package com.iasoftwares.sharelist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.List;

public class AdapterItems extends RecyclerView.Adapter<AdapterItems.MyViewHolder> {

    List<ProdutosLista> movimentacoes;
    OnClick onClick;
    Context context;

    public AdapterItems(List<ProdutosLista> movimentacoes, Context context, OnClick onClick) {
        this.movimentacoes = movimentacoes;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_products, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProdutosLista movimentacao = movimentacoes.get(position);

        holder.descricao.setText(movimentacao.getDescricao());
        holder.quantidade.setText(movimentacao.getQuantidade());
        holder.categoria.setText(movimentacao.getCategoria());
        holder.observacao.setText(movimentacao.getObservacao());
        holder.unit.setText(movimentacao.getUnd());
        holder.deletarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.DeletaItem(position);
            }
        });
        holder.editarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.EditarItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao, quantidade, categoria, observacao, unit;
        ImageView deletarItem, editarItem;


        public MyViewHolder(View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.textAdapterDescricao);
            quantidade = itemView.findViewById(R.id.textAdapterQuantidade);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
            observacao = itemView.findViewById(R.id.textAdapterObservacao);
            deletarItem = itemView.findViewById(R.id.deleteID);
            editarItem = itemView.findViewById(R.id.editID);
            unit = itemView.findViewById(R.id.textAdapterProdUN);
        }

    }

}

