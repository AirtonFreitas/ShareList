package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.adapter.AdapterItemsMarket;
import com.iasoftwares.sharelist.adapter.OnClick;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.ArrayList;
import java.util.List;

public class GoMarketItemsActivity extends AppCompatActivity implements OnClick{
    private RecyclerView recyclerView;
    private AdapterItemsMarket adapterItemsMarket;
    private List<ProdutosLista> produtos = new ArrayList<>();
    private ProdutosLista prodLista;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerLista;
    private TextView nameList;
    private Button btnVoltarItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_market_items);
        recyclerView = findViewById(R.id.recyclerProdutosGoID);
        nameList = findViewById(R.id.textView6);
        btnVoltarItems = findViewById(R.id.btnBackListID);


        adapterItemsMarket = new AdapterItemsMarket(produtos, this, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterItemsMarket);

        Intent intent = getIntent();
        String recebida = intent.getStringExtra("chosenList");
        recuperarItens(recebida);
        nameList.setText(recebida);

        btnVoltarItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoMarketItemsActivity.this, GoMarketListActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void deleteList(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir item?");
        alertDialog.setIcon(R.drawable.ic_baseline_delete_forever_gray);
        alertDialog.setMessage("Você tem certeza que deseja excluir esse produto da sua lista?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prodLista = produtos.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);
                movimentacaoRef = firebaseRef.child("Listas")
                        .child(idUsuario)
                        .child(prodLista.getNomeLista());
                movimentacaoRef.child(prodLista.getKey()).removeValue();
                adapterItemsMarket.notifyItemRemoved(position);
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(GoMarketItemsActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterItemsMarket.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void recuperarItens(String recebida) {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("Listas").child(idUsuario).child(recebida);
        valueEventListenerLista = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                produtos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    ProdutosLista prodLista = dados.getValue(ProdutosLista.class);
                    prodLista.setKey(dados.getKey());
                    produtos.add(prodLista);
                }
                adapterItemsMarket.notifyDataSetChanged();
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

    @Override
    public void DeletaItem(int position) {
        deleteList(position);
    }

    @Override
    public void EditarItem(int position) {
        checkedItem(position);
    }

    private void checkedItem(int position) {
        prodLista = produtos.get(position);
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("Listas")
                .child(idUsuario)
                .child(prodLista.getNomeLista());

        String a = movimentacaoRef.child(prodLista.getKey()).child("status").get().toString();
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();

        if (movimentacaoRef.child(prodLista.getKey()).child("status").toString() == "S") {


            valueEventListenerLista = movimentacaoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        ProdutosLista prodLista = dados.getValue(ProdutosLista.class);
                        prodLista.setKey(dados.getKey());
                        produtos.add(prodLista);
                    }
                };

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            movimentacaoRef.child(prodLista.getKey()).child("status").setValue("S");
        } else {
            movimentacaoRef.child(prodLista.getKey()).child("status").setValue("N");
            valueEventListenerLista = movimentacaoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
                        ProdutosLista prodLista = dados.getValue(ProdutosLista.class);
                        prodLista.setKey(dados.getKey());
                        produtos.add(prodLista);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    @Override
    public void EscolheLista(int position) {

    }




}