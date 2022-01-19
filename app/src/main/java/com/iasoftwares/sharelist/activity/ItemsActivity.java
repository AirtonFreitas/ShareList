package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.DialogEditItem;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.adapter.AdapterMovimentacao;
import com.iasoftwares.sharelist.adapter.OnClick;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.ArrayList;
import java.util.List;

public class ItemsActivity extends AppCompatActivity implements OnClick, DialogEditItem.DialogListener {
    private RecyclerView recyclerView;
    private AdapterMovimentacao adapterMovimentacao;
    private List<ProdutosLista> produtos = new ArrayList<>();
    private ProdutosLista prodLista;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerLista;
    private TextView textDescrp, textQtd, textViewPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_lists);
        recyclerView = findViewById(R.id.recyclerProdutosID);
        textDescrp = findViewById(R.id.textViewDescr);
        textQtd = findViewById(R.id.textViewQuant);
        textViewPos = findViewById(R.id.textViewPosition);

        adapterMovimentacao = new AdapterMovimentacao(produtos, this, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);
        ;
        Intent intent = getIntent();
        String recebida = intent.getStringExtra("chosenList");
        //Toast.makeText(this, "A lista selecionada é a : "+ recebida, Toast.LENGTH_LONG).show();
        recuperarItens(recebida);
    }

    public void deleteItem(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Produto da Lista?");
        alertDialog.setIcon(R.drawable.ic_baseline_delete);
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
                adapterMovimentacao.notifyItemRemoved(position);
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

    private void recuperarItens(String recebida) {


        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("Listas").child(idUsuario).child(recebida);
        //Toast.makeText(this, "O valor de movimentacaoRef é : "+movimentacaoRef.getKey(), Toast.LENGTH_LONG).show();
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

    @Override
    public void DeletaItem(int position) {
        deleteItem(position);
    }

    @Override
    public void EditarItem(int position) {

        openDialogEdit();
        editItem(position);
    }

    @Override
    public void EscolheLista(int position) {

    }

    private void openDialogEdit() {
        DialogEditItem dialogEditItem = new DialogEditItem();
        dialogEditItem.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void appyText(String newDescription, int newQtd) {
        }

    public void editItem(int position) {
        prodLista = produtos.get(position);

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        movimentacaoRef = firebaseRef.child("Listas")
                .child(idUsuario)
                .child(prodLista.getNomeLista());

        movimentacaoRef.child(prodLista.getKey()).child("descricao").setValue("textDescrp.getText().toString()");
        movimentacaoRef.child(prodLista.getKey()).child("quantidade").setValue("textQtd.getText().toString()");
    }

}