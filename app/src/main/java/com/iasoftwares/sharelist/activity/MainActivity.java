package com.iasoftwares.sharelist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.Usuario;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private LinearLayout squareone, squaretwo, squarethree, squarefour;
    private DatabaseReference userLogado;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        squareone = findViewById(R.id.square_one);
        squaretwo = findViewById(R.id.square_two);
        squarethree = findViewById(R.id.square_three);
        squarefour = findViewById(R.id.square_four);
        welcomeText = findViewById(R.id.welcomeTextID);


        autenticacao = SettingsFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            String emailUsuario = autenticacao.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);
            userLogado = firebaseRef.child("usuarios").child(idUsuario);
            userLogado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario nameUser = snapshot.getValue(Usuario.class);
                    String namestr = nameUser.getNome();
                    welcomeText.setText("Seja bem vindo, " + namestr + "!");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        squareone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewList();
            }
        });
        squaretwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OldLists();
            }
        });
        squarethree.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View v) {
                CustomList();
            }
        });
        squarefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoMarket();
            }
        });

    }


    public void NewList() {
        Intent intent = new Intent(MainActivity.this, RegisterItemsActivity.class);
        startActivity(intent);
    }

    public void OldLists() {
        Intent intent = new Intent(MainActivity.this, ListsActivity.class);
        startActivity(intent);
    }

    public void CustomList() {
        Intent intent = new Intent(MainActivity.this, EasyListActivity.class);
        startActivity(intent);
    }

    public void GoMarket() {
        Intent intent = new Intent(MainActivity.this, GoMarketListActivity.class);
        startActivity(intent);
    }

}