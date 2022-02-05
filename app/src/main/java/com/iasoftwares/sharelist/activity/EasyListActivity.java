package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private LinearLayout list1, list2, list3, list4, list5, list6;
    private ProdutosLista prodLista;
    private List produtos = new ArrayList<>();
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();
    private DatabaseReference fromPath, toPath;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_list);
        bntBack = findViewById(R.id.btnBackID);
        list1 = findViewById(R.id.linearLayout1);
        bntBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EasyListActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    public void copyList(DatabaseReference fromPath, final DatabaseReference toPath, String chosenList) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Copiar Lista");
        alertDialog.setIcon(R.drawable.ic_baseline_folder_copy);
        alertDialog.setMessage("Você tem certeza que deseja copiar a lista de " + chosenList + " ?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(EasyListActivity.this, "copiado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EasyListActivity.this, "não copiado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                };
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
                        Toast.makeText(EasyListActivity.this, "1° quadro", Toast.LENGTH_SHORT).show();
                        toPath = firebaseRef.child("listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Lista Básica");
                        fromPath = firebaseRef.child("listas").child(idUsuario).child("Lista Básica");
                        chosenList = "Lista Básica";
                        break;
                    case "1":
                        Toast.makeText(EasyListActivity.this, "2° quadro", Toast.LENGTH_SHORT).show();
                        fromPath = firebaseRef.child("listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Lista Gourmet" );
                        toPath = firebaseRef.child("listas").child(idUsuario).child("Lista Gourmet");
                        chosenList = "Lista Gourmet";
                        break;
                    case "2":
                        Toast.makeText(EasyListActivity.this, "3° quadro", Toast.LENGTH_SHORT).show();
                        fromPath = firebaseRef.child("listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Limpeza");
                        toPath = firebaseRef.child("listas").child(idUsuario).child("Limpeza");
                        chosenList = "Limpeza";
                        break;
                    case "3":
                        Toast.makeText(EasyListActivity.this, "4° quadro", Toast.LENGTH_SHORT).show();
                        fromPath = firebaseRef.child("listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Churrasco");
                        toPath = firebaseRef.child("listas").child(idUsuario).child("Churrasco");
                        chosenList = "Churrasco";
                        break;
                    case "4":
                        Toast.makeText(EasyListActivity.this, "5° quadro", Toast.LENGTH_SHORT).show();
                        fromPath = firebaseRef.child("listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Hortifruti");
                        toPath = firebaseRef.child("listas").child(idUsuario).child("Hortifruti");
                        chosenList = "Hortifruti";
                        break;
                    case "5":
                        Toast.makeText(EasyListActivity.this, "6° quadro", Toast.LENGTH_SHORT).show();
                        fromPath = firebaseRef.child("listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Vegana");
                        toPath = firebaseRef.child("listas").child(idUsuario).child("Vegana");
                        chosenList = "Vegana";
                        break;
                    default:
                        Toast.makeText(EasyListActivity.this, "nenhum ", Toast.LENGTH_SHORT).show();
                }

                copyList(fromPath, toPath, chosenList);

            }
        }