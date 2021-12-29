package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.adapter.AdapterMovimentacao;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<ProdutosLista> produtos = new ArrayList<>();
    private ProdutosLista prodLista;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerLista;

    ProdutosLista produtoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_list);
        recyclerView = findViewById(R.id.recyclerProdutosID);

        adapterMovimentacao = new AdapterMovimentacao(produtos, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
        recuperarItens();


        /*adapterMovimentacao.onBindViewHolder();
        RecyclerView.ViewHolder viewHolder = null;
        excluirMovimentacao(viewHolder);*/



    }




    public void excluirMovimentacao(RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Produto da Lista?");
        alertDialog.setIcon(R.drawable.ic_baseline_delete_forever);
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir esse produto da sua lista?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                prodLista = produtos.get( position );

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64( emailUsuario );
                movimentacaoRef = firebaseRef.child("movimentacao")
                        .child( idUsuario )
                        .child(produtoSelecionado.getNomeLista() );

                movimentacaoRef.child( prodLista.getKey() ).removeValue();
                adapterMovimentacao.notifyItemRemoved( position );

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ItemsActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }
    private void recuperarItens() {

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("Listas").child(idUsuario).child("aaa");

        valueEventListenerLista = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produtos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProdutosLista prodLista = dados.getValue(ProdutosLista.class);
                    prodLista.setKey(dados.getKey());
                    produtos.add(prodLista);
                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        movimentacaoRef.removeEventListener(valueEventListenerLista);
    }
}