package com.iasoftwares.sharelist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.List;

public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

    List<ProdutosLista> movimentacoes;
    OnClick onClick;
    Context context;

    public AdapterMovimentacao(List<ProdutosLista> movimentacoes, Context context, OnClick onClick) {
        this.movimentacoes = movimentacoes;
        this.context = context;
        this.onClick = onClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProdutosLista movimentacao = movimentacoes.get(position);

        holder.descricao.setText(movimentacao.getDescricao());
        holder.quantidade.setText(movimentacao.getQuantidade());
        holder.categoria.setText(movimentacao.getCategoria());
        holder.observacao.setText(movimentacao.getObservacao());

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

        //holder.status.setText(movimentacao.getStatus());
        //holder.nomeLista.setText(movimentacao.getNomeLista());

        //holder.deletarItem.setOnClickListener(v -> removerItem(position));


        //colore de azul ou vermelho a movimentação
        /*if (movimentacao.getStatus() == "d" || movimentacao.getStatus().equals("d")) {
            holder.quantidade.setTextColor(context.getResources().getColor(R.color.pur));
            //holder.quantidade.setText("-" + movimentacao.getValor());
        }
        */
    }

    private void removerItem(int position) {

        DatabaseReference movimentacaoRef = SettingsFirebase.getFirebaseDatabase();
        movimentacaoRef.removeValue();
        movimentacaoRef.getKey();
        movimentacoes.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movimentacoes.size());

    }

    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView descricao, quantidade, categoria, observacao;
        ImageView deletarItem, editarItem;


        public MyViewHolder(View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.textAdapterDescricao);
            quantidade = itemView.findViewById(R.id.textAdapterQuantidade);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
            observacao = itemView.findViewById(R.id.textAdapterObservacao);
            deletarItem = itemView.findViewById(R.id.deleteID);
            editarItem = itemView.findViewById(R.id.editID);


            /*if(descricao.getText().toString() == "Teste"){
                statuscheckbox.setChecked(true);
            }else{
                statuscheckbox.setChecked(false);
                quantidade.setPaintFlags(quantidade.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }*/
        }

    }

}

