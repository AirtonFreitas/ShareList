package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_list);
        bntBack = findViewById(R.id.btnBackID);
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
                                    Toast.makeText(EasyListActivity.this, "Lista " + fromPath.getKey() + " copiada com sucesso! ", Toast.LENGTH_LONG).show();
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
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Básica");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("Básica");
                chosenList = "Básica";
                break;
            case "1":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Gourmet");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("Gourmet");
                chosenList = "Gourmet";
                break;
            case "2":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Limpeza");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("Limpeza");
                chosenList = "Limpeza";
                break;
            case "3":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Churrasco");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("Churrasco");
                chosenList = "Churrasco";
                break;
            case "4":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Hortifruti");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("Hortifruti");
                chosenList = "Hortifruti";
                break;
            case "5":
                fromPath = firebaseRef.child("Listas").child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg==").child("Vegana");
                toPath = firebaseRef.child("Listas").child(idUsuario).child("Vegana");
                chosenList = "Vegana";
                break;
            default:
                Toast.makeText(EasyListActivity.this, "nenhum ", Toast.LENGTH_SHORT).show();
        }
        copyList(toPath, fromPath, chosenList);
    }
}