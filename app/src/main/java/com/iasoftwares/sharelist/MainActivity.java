package com.iasoftwares.sharelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.iasoftwares.sharelist.config.SettingsFirebase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autenticacao = SettingsFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }


}