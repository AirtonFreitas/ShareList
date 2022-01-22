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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
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
    private DatabaseReference movimentacaoRef;
    private DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();





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

    public void copyList(String chosenList){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Copiar Lista");
        alertDialog.setIcon(R.drawable.ic_baseline_delete);
        alertDialog.setMessage("Você tem certeza que deseja copiar a lista de " + chosenList + " ?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

switch (chosenList){
    case "Lista Básica":
        Toast.makeText(EasyListActivity.this, "CÓDIGO CAIXA 1" + chosenList, Toast.LENGTH_SHORT).show();
        FirebaseAuth autentic = SettingsFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autentic.getCurrentUser().getEmail());

        movimentacaoRef = firebaseRef.child("Listas")
                .child(idUsuario)
                .child(chosenList).push().setValue(firebaseRef.child("Listas")
                        .child("YWlydGl0b0Bob3RtYWlsLmNvbS5icg=="));








        break;
    case "Lista Gourmet":
        Toast.makeText(EasyListActivity.this, "CÓDIGO CAIXA 2" + chosenList, Toast.LENGTH_SHORT).show();
        break;
    case "Limpeza":
        Toast.makeText(EasyListActivity.this, "CÓDIGO CAIXA 3" + chosenList, Toast.LENGTH_SHORT).show();
        break;
    case "Churrasco":
        Toast.makeText(EasyListActivity.this, "CÓDIGO CAIXA 4" + chosenList, Toast.LENGTH_SHORT).show();
        break;
    case "Hortifruti":
        Toast.makeText(EasyListActivity.this, "CÓDIGO CAIXA 5" + chosenList, Toast.LENGTH_SHORT).show();
        break;
    case "Vegana":
        Toast.makeText(EasyListActivity.this, "CÓDIGO CAIXA 6" + chosenList, Toast.LENGTH_SHORT).show();
        break;

}
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
        switch (tag){
            case "0":
                Toast.makeText(EasyListActivity.this, "0° quadro", Toast.LENGTH_SHORT).show();
                copyList("Lista Básica");;
                break;
            case "1":
                Toast.makeText(EasyListActivity.this, "1° quadro", Toast.LENGTH_SHORT).show();
                copyList("Lista Gourmet");
                break;
            case "2":
                Toast.makeText(EasyListActivity.this, "2° quadro", Toast.LENGTH_SHORT).show();
                copyList("Limpeza");
                break;
            case "3":
                Toast.makeText(EasyListActivity.this, "3° quadro", Toast.LENGTH_SHORT).show();
                copyList("Churrasco");
                break;
            case "4":
                Toast.makeText(EasyListActivity.this, "4° quadro", Toast.LENGTH_SHORT).show();
                copyList("Hortifruti");
                break;
            case "5":
                Toast.makeText(EasyListActivity.this, "4° quadro", Toast.LENGTH_SHORT).show();
                copyList("Vegana");


                break;
            default:
                Toast.makeText(EasyListActivity.this, "nenhum ", Toast.LENGTH_SHORT).show();


        }



    }
}