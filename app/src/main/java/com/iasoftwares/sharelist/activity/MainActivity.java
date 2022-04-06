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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.helper.Base64Custom;
import com.iasoftwares.sharelist.model.Usuario;

import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private LinearLayout squareone, squaretwo, squarethree, squarefour;
    private DatabaseReference userLogado;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
    private TextView welcomeText;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        squareone = findViewById(R.id.square_one);
        squaretwo = findViewById(R.id.square_two);
        squarethree = findViewById(R.id.square_three);
        squarefour = findViewById(R.id.square_four);
        welcomeText = findViewById(R.id.welcomeTextID);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        autenticacao = SettingsFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String emailUsuario = autenticacao.getCurrentUser().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuario);
            userLogado = firebaseRef.child("usuarios").child(idUsuario);
            userLogado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario nameUser = snapshot.getValue(Usuario.class);
                    String namestr = nameUser.getNome();
                    welcomeText.setText("Olá, " + namestr + "!");
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

    }

    public void OldLists() {
        Intent intent = new Intent(this, ListsActivity.class);
        startActivity(intent);
    }

    public void CustomList() {
        Intent intent = new Intent(this, EasyListActivity.class);
        startActivity(intent);
    }

    public void GoMarket() {
        Intent intent = new Intent(this, GoMarketListActivity.class);
        startActivity(intent);
    }

    public void GoDonate() {
        Intent intent = new Intent(this, DonateActivity.class);
        startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Desconectado!", Toast.LENGTH_LONG).show();

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