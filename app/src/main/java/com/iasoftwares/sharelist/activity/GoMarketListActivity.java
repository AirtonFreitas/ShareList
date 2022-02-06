package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.adapter.AdapterListMarket;
import com.iasoftwares.sharelist.adapter.OnClick;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.ProdutosLista;
import java.util.ArrayList;
import java.util.List;

public class GoMarketListActivity extends AppCompatActivity implements OnClick {
    private RecyclerView recyclerViewMarket;
    private AdapterListMarket adapterListMarket;
    private List produtos = new ArrayList<>();
    private ProdutosLista prodLista;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();
    private DatabaseReference movimentacaoRef;
    private ValueEventListener valueEventListenerLista;
    private Button btnVoltarList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_market);
        recyclerViewMarket = findViewById(R.id.recyclerListasMarketID);
        adapterListMarket = new AdapterListMarket(produtos, this, this);
        btnVoltarList = findViewById(R.id.btnBackListID);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        btnVoltarList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoMarketListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void recuperarListasMarket() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMarket.setLayoutManager(layoutManager);
        recyclerViewMarket.setHasFixedSize(true);
        recyclerViewMarket.setAdapter(adapterListMarket);

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
                adapterListMarket.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openList(int position) {
        prodLista = (ProdutosLista) produtos.get(position);
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        movimentacaoRef = firebaseRef.child("Listas")
                .child(idUsuario).child(prodLista.getKey());
        String listSelected = movimentacaoRef.getKey();
        Intent intent = new Intent(this, GoMarketItemsActivity.class);
        intent.putExtra("chosenList", listSelected);
        startActivity(intent);
    }

    public void deleteList(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Lista?");
        alertDialog.setIcon(R.drawable.ic_baseline_delete_forever_gray);
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
                adapterListMarket.notifyItemRemoved(position);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(GoMarketListActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterListMarket.notifyDataSetChanged();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        recuperarListasMarket();
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

    @Override
    public void desmarcaItem(int position) {}

    @Override
    public void marcaItem(int position) {}
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbarNewList:
                NewList();
                break;
            case R.id.toolbarListsOld:
                OldLists();
                break;
            case R.id.toolbarCustomList:
                CustomList();
                break;
            case R.id.toolbarMarket:
                GoMarket();
                break;
            case R.id.toolbarDonate:
                GoDonate();
                break;
            case R.id.toolbarDisconnect:
                Disconnect();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    public void NewList() {
        Intent intent = new Intent(this, RegisterItemsActivity.class);
        startActivity(intent);
        finish();
    }

    public void OldLists() {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
        finish();
    }

    public void CustomList() {
        Intent intent = new Intent(this, EasyListActivity.class);
        startActivity(intent);
        finish();
    }

    public void GoMarket() {
        Intent intent = new Intent(this, GoMarketListActivity.class);
        startActivity(intent);
        finish();
    }

    public void GoDonate() {
        Intent intent = new Intent(this, DonateActivity.class);
        startActivity(intent);
        finish();
    }
    public void Disconnect() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Fazer Logout?");
        alertDialog.setIcon(R.drawable.ic_baseline_exit_to_app);
        alertDialog.setMessage("Você tem certeza que deseja deslogar do aplicativo?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                autenticacao.signOut();
                Intent intent = new Intent(GoMarketListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(GoMarketListActivity.this, "Desconectado!", Toast.LENGTH_LONG).show();

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

}