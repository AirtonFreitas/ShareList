package com.iasoftwares.sharelist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.List;

public class AdapterItemsMarket extends RecyclerView.Adapter<AdapterItemsMarket.MyViewHolder> {

    List<ProdutosLista> movimentacoes;
    OnClick onClick;
    Context context;

    public AdapterItemsMarket(List<ProdutosLista> movimentacoes, Context context, OnClick onClick) {
        this.movimentacoes = movimentacoes;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_market_products, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int pos) {
        ProdutosLista movimentacao = movimentacoes.get(pos);


        holder.descricao.setText(movimentacao.getDescricao());
        holder.quantidade.setText(movimentacao.getQuantidade());
        holder.categoria.setText(movimentacao.getCategoria());
        holder.observacao.setText(movimentacao.getObservacao());
        holder.deletarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.DeletaItem(pos);
            }
        });

        if (movimentacao.getStatus().equals("S")) {
            holder.descricao.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.categoria.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.observacao.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkItem.setBackgroundResource(R.drawable.ic_baseline_check_box);
        } else {
            holder.descricao.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.categoria.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.observacao.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.checkItem.setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank);
        }
        holder.checkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movimentacao.getStatus().equals("S")) {
                    onClick.desmarcaItem(pos);
                } else {
                    onClick.marcaItem(pos);

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao, quantidade, categoria, observacao;
        ImageView deletarItem, checkItem;


        public MyViewHolder(View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.textAdapterDescricao);
            quantidade = itemView.findViewById(R.id.textAdapterQuantidade);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
            observacao = itemView.findViewById(R.id.textAdapterObservacao);
            deletarItem = itemView.findViewById(R.id.deleteID);
            checkItem = itemView.findViewById(R.id.checkedImg);
        }

    }

}

