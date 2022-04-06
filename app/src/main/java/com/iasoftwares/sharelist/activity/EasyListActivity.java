package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.ProdutosLista;

import java.util.ArrayList;
import java.util.List;

public class EasyListActivity extends AppCompatActivity {
    private Button bntBack;
    private DatabaseReference toPath, fromPath;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private Toolbar toolbar;
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_list);
        bntBack = findViewById(R.id.btnBackID);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        bntBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EasyListActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    public void copyList(DatabaseReference toPath, final DatabaseReference fromPath, String chosenList) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Copiar Lista");
        alertDialog.setIcon(R.drawable.ic_baseline_folder_copy);
        alertDialog.setMessage("Você tem certeza que deseja copiar a lista " + chosenList + " ?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ValueEventListener valueEventListener = fromPath.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(EasyListActivity.this, "Lista " + fromPath.getKey() + " copiada com sucesso! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EasyListActivity.this, ItemsActivity.class);
                                    intent.putExtra("chosenList", chosenList);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(EasyListActivity.this, "Houve algum erro e não foi possível copiar a lista " + fromPath.getKey(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                fromPath.addListenerForSingleValueEvent(valueEventListener);
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

    public void selectList(View linearLayout) {
        String tag = linearLayout.getTag().toString();
        FirebaseAuth autentic = SettingsFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autentic.getCurrentUser().getEmail());
        String chosenList = "aaa";
        switch (tag) {
            case "0":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("ListaAppBásica");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("ListaAppBásica");
                chosenList = "ListaAppBásica";
                break;
            case "1":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("ListaAppGourmet");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("ListaAppGourmet");
                chosenList = "ListaAppGourmet";
                break;
            case "2":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("ListaAppLimpeza");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("ListaAppLimpeza");
                chosenList = "ListaAppLimpeza";
                break;
            case "3":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("ListaAppChurrasco");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("ListaAppChurrasco");
                chosenList = "ListaAppChurrasco";
                break;
            case "4":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("ListaAppHortifruti");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("ListaAppHortifruti");
                chosenList = "ListaAppHortifruti";
                break;
            case "5":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("ListaAppVegana");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("ListaAppVegana");
                chosenList = "ListaAppVegana";
                break;
            default:
                Toast.makeText(EasyListActivity.this, "nenhum ", Toast.LENGTH_SHORT).show();
        }
        copyList(toPath, fromPath, chosenList);
    }

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
                Intent intent = new Intent(EasyListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(EasyListActivity.this, "Desconectado!", Toast.LENGTH_LONG).show();


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