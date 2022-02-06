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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.iasoftwares.sharelist.DialogQuestionNameList;
import com.iasoftwares.sharelist.R;
import com.iasoftwares.sharelist.config.SettingsFirebase;
import com.iasoftwares.sharelist.model.ProdutosLista;

public class RegisterItemsActivity extends AppCompatActivity implements DialogQuestionNameList.DialogListener {
    private Spinner spinnerCategories, spinnerUN;
    private EditText descricao, observacao, quantidade;
    private ProdutosLista produtosLista;
    private Button btnSave, btnBack;
    private Toolbar toolbar;
    private TextView textViewNameList;
    private FirebaseAuth autenticacao = SettingsFirebase.getFirebaseAutenticacao();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_items);
        spinnerCategories = findViewById(R.id.spinnerCateg);
        spinnerUN = findViewById(R.id.spinnerUN);
        descricao = findViewById(R.id.descricaoItemID);
        observacao = findViewById(R.id.observacaoItemID);
        quantidade = findViewById(R.id.quantidadeItemID);
        btnSave = findViewById(R.id.btnSaveID);
        btnBack = findViewById(R.id.btnBackID);
        textViewNameList = findViewById(R.id.textViewNameList);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        openDialog();


        AtivarSpinnerCateg();
        AtivarSpinnerUn();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GravarItem();
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterItemsActivity.this, ListsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void GravarItem() {
        String textDescricao = descricao.getText().toString();
        if (!textDescricao.isEmpty()) {
            salvarLista();
            Toast.makeText(RegisterItemsActivity.this, "Item gravado com sucesso", Toast.LENGTH_SHORT).show();
            descricao.setText("");
            observacao.setText("");
            quantidade.setText("");
            descricao.requestFocus();
            AtivarSpinnerCateg();
            AtivarSpinnerUn();
        } else {
            descricao.setError("Campo vazio!");
            descricao.requestFocus();
        }
    }

    private void AtivarSpinnerUn() {
        String[] arraySpinnerUn = new String[]{
                "UN", "KG", "LT", "CX", "PCT", "RL"
        };
        Spinner spinnerUnid = (Spinner) findViewById(R.id.spinnerUN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerUn);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnid.setAdapter(adapter);
    }

    private void openDialog() {
        DialogQuestionNameList dialogQuestionNameList = new DialogQuestionNameList();
        dialogQuestionNameList.show(getSupportFragmentManager(), "Dialog");

    }

    private void AtivarSpinnerCateg() {
        String[] arraySpinner = new String[]{
                "Escolha a Categoria", "Alimentos", "Bebidas", "Bebidas Alcoólicas", "Congelados e frios", "Higiene Pessoal", "Hortifruti", "Padaria", "Produtos de Limpeza", "Outros"
        };
        Spinner spinnerCategories = (Spinner) findViewById(R.id.spinnerCateg);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);
    }

    public void salvarLista() {
        produtosLista = new ProdutosLista();
        produtosLista.setNomeLista(textViewNameList.getText().toString());
        produtosLista.setDescricao(descricao.getText().toString());
        produtosLista.setCategoria(spinnerCategories.getSelectedItem().toString());
        produtosLista.setUnd(spinnerUN.getSelectedItem().toString());
        produtosLista.setObservacao(observacao.getText().toString());
        produtosLista.setQuantidade(quantidade.getText().toString());
        produtosLista.setStatus("N");
        produtosLista.salvar();
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
                Intent intent = new Intent(RegisterItemsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(RegisterItemsActivity.this, "Desconectado!", Toast.LENGTH_LONG).show();

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
    @Override
    public void appyText(String nameList) {
        textViewNameList.setText(nameList);
    }
}