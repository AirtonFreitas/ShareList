package com.iasoftwares.sharelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.adapter.AdapterMovimentacao;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.ArrayList;
import java.util.List;

public class OldListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<ProdutosLista> produtos = new ArrayList<>();
    private ProdutosLista prodLista;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerLista;


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

    }

    private void recuperarMovimentacoes() {
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("Listas").child(idUsuario).child("Segunda Lista");

        valueEventListenerLista = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        movimentacaoRef.removeEventListener(valueEventListenerLista);
    }
}