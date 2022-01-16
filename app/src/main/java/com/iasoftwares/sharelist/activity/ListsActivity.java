package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.adapter.AdapterList;
import com.iasoftwares.sharelist.adapter.AdapterMovimentacao;
import com.iasoftwares.sharelist.adapter.OnClick;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.ArrayList;
import java.util.List;

public class ListsActivity extends AppCompatActivity implements OnClick {
    private RecyclerView recyclerView;
    private AdapterList adapterList;
    private List produtos = new ArrayList<>();
    private ProdutosLista prodLista;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        recyclerView = findViewById(R.id.recyclerListasID);
        adapterList = new AdapterList(produtos, this, this);


    }

    private void recuperarListas() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterList);

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("Listas").child(idUsuario);

        valueEventListenerLista = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produtos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProdutosLista prodLista = dados.getValue(ProdutosLista.class);
                    prodLista.setKey(dados.getKey());
                    produtos.add(prodLista);
                }
                adapterList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void deleteList(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Lista?");
        alertDialog.setIcon(R.drawable.ic_baseline_delete);
        alertDialog.setMessage("Você tem certeza que deseja excluir essa lista?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prodLista = (ProdutosLista) produtos.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                movimentacaoRef = firebaseRef.child("Listas")
                        .child(idUsuario);
                movimentacaoRef.child(prodLista.getKey()).removeValue();
                adapterList.notifyItemRemoved(position);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListsActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterList.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void openList(int position) {
        prodLista = (ProdutosLista) produtos.get(position);

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        movimentacaoRef = firebaseRef.child("Listas")
                .child(idUsuario).child(prodLista.getKey());

        String listSelected = movimentacaoRef.toString();
        listSelected.substring(20);

        Toast.makeText(this, "A lista selecionada é a : " + listSelected, Toast.LENGTH_LONG).show();


        /*Intent intent = new Intent(this, ItemsActivity.class);
        intent.putExtra("chosenList", (Parcelable) movimentacaoRef);
        startActivity(intent);
        finishAffinity();*/


    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarListas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        movimentacaoRef.removeEventListener(valueEventListenerLista);
    }

    @Override
    public void DeletaItem(int position) {
        deleteList(position);
    }

    @Override
    public void EditarItem(int position) {

    }

    @Override
    public void EscolheLista(int position) {
openList(position);
    }


}