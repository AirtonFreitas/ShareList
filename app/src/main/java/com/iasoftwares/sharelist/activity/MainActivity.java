package com.iasoftwares.sharelist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private LinearLayout squareone, squaretwo, squarethree, squarefour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        squareone = findViewById(R.id.square_one);
        squaretwo = findViewById(R.id.square_two);
        squarethree = findViewById(R.id.square_three);
        squarefour = findViewById(R.id.square_four);

        autenticacao = SettingsFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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
        Intent intent = new Intent(MainActivity.this, ItemsActivity.class);
        startActivity(intent);
    }

    public void CustomList() {
        Intent intent = new Intent(MainActivity.this, CustomListActivity.class);
        startActivity(intent);
    }

    public void GoMarket() {
        Intent intent = new Intent(MainActivity.this, GoMarketActivity.class);
        startActivity(intent);
    }

}